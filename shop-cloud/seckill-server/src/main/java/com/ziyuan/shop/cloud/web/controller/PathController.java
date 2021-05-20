package com.ziyuan.shop.cloud.web.controller;

import com.ziyuan.shop.cloud.domain.User;
import com.ziyuan.shop.cloud.exception.BusinessException;
import com.ziyuan.shop.cloud.redis.key.SeckillRedisKey;
import com.ziyuan.shop.cloud.resp.Result;
import com.ziyuan.shop.cloud.service.ISeckillGoodService;
import com.ziyuan.shop.cloud.util.CookieUtil;
import com.ziyuan.shop.cloud.vo.SeckillGoodVo;
import com.ziyuan.shop.cloud.web.SeckillCodeMsg;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/api/path")
public class PathController extends BaseController {

    @Autowired
    private ISeckillGoodService seckillGoodService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping
    public Result<String> getPath(Long seckillId, String verifyCode, @CookieValue(CookieUtil.TOKEN_IN_COOKIE) String userToken) {
        User user = getCurrentUser(userToken);
        if (user == null || seckillId == null) {
            throw new BusinessException(SeckillCodeMsg.OP_ERROR);
        }
        
        String verifyCodeInRedis = redisTemplate.opsForValue().get(SeckillRedisKey.SECKILL_VERIFY_CODE.join(seckillId + "", user.getId() + ""));
        if (StringUtils.isEmpty(verifyCodeInRedis) || !verifyCodeInRedis.equals(verifyCode)) {
            throw new BusinessException(SeckillCodeMsg.VERIFY_CODE_ERROR);
        }

        
        redisTemplate.delete(SeckillRedisKey.SECKILL_VERIFY_CODE.join(seckillId + "", user.getId() + ""));

        SeckillGoodVo vo = seckillGoodService.detail(seckillId);
        Date now = new Date();
        if (now.compareTo(vo.getStartDate()) < 0) {
            throw new BusinessException(SeckillCodeMsg.NOT_START_ERROR);
        }
        // now: 10:00   end: 09:00
        if (now.compareTo(vo.getEndDate()) > 0) {
            throw new BusinessException(SeckillCodeMsg.END_ERROR);
        }
        String random = UUID.randomUUID().toString().replaceAll("-", "");

        redisTemplate.opsForValue().set(
                SeckillRedisKey.SECKILL_PATH_RANDOM.join(seckillId + "", user.getId() + ""),
                random,
                SeckillRedisKey.SECKILL_PATH_RANDOM.getExpireTime(),
                SeckillRedisKey.SECKILL_PATH_RANDOM.getUnit());

        return Result.success(random);
    }
}
