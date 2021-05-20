package com.ziyuan.shop.cloud.web.controller;

import com.ziyuan.shop.cloud.domain.OrderInfo;
import com.ziyuan.shop.cloud.domain.User;
import com.ziyuan.shop.cloud.exception.BusinessException;
import com.ziyuan.shop.cloud.mq.MQConstants;
import com.ziyuan.shop.cloud.mq.MQLogSendCallback;
import com.ziyuan.shop.cloud.mq.msg.CreateSeckillOrderMsg;
import com.ziyuan.shop.cloud.redis.key.SeckillRedisKey;
import com.ziyuan.shop.cloud.resp.Result;
import com.ziyuan.shop.cloud.service.IOrderInfoService;
import com.ziyuan.shop.cloud.service.ISeckillGoodService;
import com.ziyuan.shop.cloud.service.ISeckillOrderService;
import com.ziyuan.shop.cloud.util.CookieUtil;
import com.ziyuan.shop.cloud.vo.SeckillGoodVo;
import com.ziyuan.shop.cloud.web.SeckillCodeMsg;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/orders")
@Slf4j
public class OrderController extends BaseController {

    public static final ConcurrentHashMap<Long, Boolean> STOCK_COUNT_FLAG = new ConcurrentHashMap<>();

    private final ISeckillGoodService seckillGoodService;
    private final IOrderInfoService orderInfoService;
    private final ISeckillOrderService seckillOrderService;
    private final RocketMQTemplate rocketMQTemplate;
    private final StringRedisTemplate redisTemplate;

    public OrderController(RocketMQTemplate rocketMQTemplate, ISeckillGoodService seckillGoodService, IOrderInfoService orderInfoService, ISeckillOrderService seckillOrderService, StringRedisTemplate  redisTemplate) {
        this.seckillGoodService = seckillGoodService;
        this.orderInfoService = orderInfoService;
        this.seckillOrderService = seckillOrderService;
        this.redisTemplate = redisTemplate;
        this.rocketMQTemplate = rocketMQTemplate;
    }

    @GetMapping("/{orderNo}")
    public Result<OrderInfo> orderDetail(@PathVariable("orderNo") String orderNo, @CookieValue(CookieUtil.TOKEN_IN_COOKIE) String token) {
        User user = this.getCurrentUser(token);
        if (user == null) {
            throw new BusinessException(SeckillCodeMsg.OP_ERROR);
        }

        OrderInfo orderInfo = orderInfoService.findById(orderNo, user.getId());
        return Result.success(orderInfo);
    }

    /**
     * @param seckillId
     * @param token
     * @param uuid
     * @return
     */
    @PostMapping
    public Result<String> doSeckill(String uuid, Long seckillId, @CookieValue(CookieUtil.TOKEN_IN_COOKIE) String token) {
        User user = this.getCurrentUser(token);
        
        if (seckillId == null || StringUtils.isEmpty(uuid) || user == null) {
            throw new BusinessException(SeckillCodeMsg.OP_ERROR);
        }

        
        Boolean over = STOCK_COUNT_FLAG.get(seckillId);
        if (over != null && over) {
            throw new BusinessException(SeckillCodeMsg.OUT_OF_STOCK_ERROR);
        }

        SeckillGoodVo vo = seckillGoodService.detailByCache(seckillId);
        if (vo == null) {
            throw new BusinessException(SeckillCodeMsg.OP_ERROR);
        }
        Date now = new Date();
        if (now.compareTo(vo.getStartDate()) < 0) {
            throw new BusinessException(SeckillCodeMsg.NOT_START_ERROR);
        }
        if (now.compareTo(vo.getEndDate()) > 0) {
            throw new BusinessException(SeckillCodeMsg.END_ERROR);
        }

        Boolean ifAbsent = redisTemplate.opsForValue().setIfAbsent(SeckillRedisKey.SECKILL_USER_RECORD.join(seckillId + "", user.getId() + ""), "ok");
        if (ifAbsent == null || !ifAbsent) {
            throw new BusinessException(SeckillCodeMsg.REPATE_ERROR);
        }

        Long remain = redisTemplate.opsForHash().increment(
                SeckillRedisKey.SECKILL_STOCK_COUNT_HASH.join(""),
                seckillId + "",
                -1
        );
        if (remain < 0) {
            STOCK_COUNT_FLAG.put(seckillId, true);
            redisTemplate.delete(SeckillRedisKey.SECKILL_USER_RECORD.join(seckillId + "", user.getId() + ""));
            throw new BusinessException(SeckillCodeMsg.OUT_OF_STOCK_ERROR);
        }
        rocketMQTemplate.asyncSend(MQConstants.CREATE_ORDER_DEST, new CreateSeckillOrderMsg(uuid, seckillId, user.getId()), new MQLogSendCallback());
        return Result.success("trying to get deal...");
    }


}
