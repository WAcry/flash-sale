package com.ziyuan.shop.cloud.redis;

import lombok.Getter;
import org.apache.commons.lang.StringUtils;

import java.util.concurrent.TimeUnit;

@Getter
public enum MemberRedisKey {
    /**
     * user Redistoken
     */
    USER_TOKEN_KEY("userToken:", 30, TimeUnit.MINUTES);

    MemberRedisKey(String prefix, long expireTime, TimeUnit unit) {
        this.prefix = prefix;
        this.expireTime = expireTime;
        this.unit = unit;
    }


    private String prefix;
    private Long expireTime;
    private TimeUnit unit;

    public String join(String ...key) {
        return this.getPrefix() + StringUtils.join(key, ":");
    }
}
