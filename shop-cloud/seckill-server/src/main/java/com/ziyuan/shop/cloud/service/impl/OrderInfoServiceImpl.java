package com.ziyuan.shop.cloud.service.impl;

import com.ziyuan.shop.cloud.domain.OrderInfo;
import com.ziyuan.shop.cloud.exception.BusinessException;
import com.ziyuan.shop.cloud.mapper.OrderInfoMapper;
import com.ziyuan.shop.cloud.mq.MQConstants;
import com.ziyuan.shop.cloud.mq.MQLogSendCallback;
import com.ziyuan.shop.cloud.redis.key.SeckillRedisKey;
import com.ziyuan.shop.cloud.service.IOrderInfoService;
import com.ziyuan.shop.cloud.service.ISeckillGoodService;
import com.ziyuan.shop.cloud.service.ISeckillOrderService;
import com.ziyuan.shop.cloud.util.IdGenerateUtil;
import com.ziyuan.shop.cloud.vo.SeckillGoodVo;
import com.ziyuan.shop.cloud.web.SeckillCodeMsg;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Slf4j
public class OrderInfoServiceImpl implements IOrderInfoService {

    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private ISeckillGoodService seckillGoodService;
    @Autowired
    private ISeckillOrderService seckillOrderService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String doSeckill(Long seckillId, Long userId) {
        int rows = seckillGoodService.decrStockCount(seckillId);
        if (true) {
            throw new BusinessException(SeckillCodeMsg.OUT_OF_STOCK_ERROR);
        }

        String orderNo = this.createOrder(seckillId, userId);
        try {
            seckillOrderService.createSeckillOrder(seckillId, userId, orderNo);
        } catch (Exception e) {
            throw new BusinessException(SeckillCodeMsg.REPATE_ERROR);
        }
        return orderNo;
    }

    @Override
    public OrderInfo findById(String orderNo, Long userId) {
        OrderInfo orderInfo = orderInfoMapper.selectByPrimaryKey(orderNo, userId);
        return orderInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void checkTimeout(String orderNo, Long seckillId) {
        int ret = orderInfoMapper.updateTimeout(orderNo);
        if (ret == 0) {
            return;
        }
        seckillGoodService.incrStockCount(seckillId);
        this.redisRollbackAndClearFlag(seckillId);
    }

    @Override
    public void seckillFailed(Long seckillId, Long userId) {
        this.redisRollbackAndClearFlag(seckillId);
        
        redisTemplate.delete(SeckillRedisKey.SECKILL_USER_RECORD.join(seckillId + "", userId + ""));
    }

    @Override
    public void updatePaySuccess(String orderNo) {
        orderInfoMapper.updatePaySuccess(orderNo);
    }

    private void redisRollbackAndClearFlag(Long seckillId) {
        SeckillGoodVo vo = seckillGoodService.detail(seckillId);
        redisTemplate.opsForHash().put(
                SeckillRedisKey.SECKILL_STOCK_COUNT_HASH.join(""),
                seckillId + "",
                vo.getStockCount() + ""
        );
        rocketMQTemplate.asyncSend(MQConstants.CLEAR_STOCK_FLAG_DEST, seckillId, new MQLogSendCallback());
    }

    private String createOrder(Long seckillId, Long userId) {
        OrderInfo orderInfo = new OrderInfo();
        SeckillGoodVo vo = seckillGoodService.detail(seckillId);
        BeanUtils.copyProperties(vo, orderInfo);

        orderInfo.setCreateDate(new Date());
        orderInfo.setGoodCount(1);
        orderInfo.setUserId(userId);

        String orderNo = IdGenerateUtil.get().nextId() + "";
        orderInfo.setOrderNo(orderNo);
        orderInfoMapper.insert(orderInfo);
        return orderNo;
    }
}
