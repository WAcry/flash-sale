package com.ziyuan.shop.cloud.redis.key;

import lombok.Getter;
import org.apache.commons.lang.StringUtils;

import java.util.concurrent.TimeUnit;

@Getter
public enum SeckillRedisKey {
    /**
     *：STRING
     */
    SECKILL_GOODS_DETAIL("seckillGoodsDetail:", 2, TimeUnit.DAYS),
    /**
     *：HASH
     */
    SECKILL_STOCK_COUNT_HASH("seckillStockCountHash"),
    /**
     */
    SECKILL_USER_RECORD("seckillUserRecord:"),
    /**
     */
    SECKILL_PATH_RANDOM("seckillPathRandom:", 2, TimeUnit.SECONDS),
    /**
     */
    SECKILL_VERIFY_CODE("seckillVerifyCode:", 5, TimeUnit.MINUTES);

    SeckillRedisKey(String prefix) {
        this(prefix, 0, null);
    }

    SeckillRedisKey(String prefix, long expireTime, TimeUnit unit) {
        this.prefix = prefix;
        this.expireTime = expireTime;
        this.unit = unit;
    }

    
    private String prefix;
    
    private Long expireTime;
    
    private TimeUnit unit;

    public String join(String... key) {
        return this.getPrefix() + StringUtils.join(key, ":");
    }
}
