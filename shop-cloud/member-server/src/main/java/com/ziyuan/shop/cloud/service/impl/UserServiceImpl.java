package com.ziyuan.shop.cloud.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ziyuan.shop.cloud.domain.User;
import com.ziyuan.shop.cloud.exception.BusinessException;
import com.ziyuan.shop.cloud.mapper.UserMapper;
import com.ziyuan.shop.cloud.redis.MemberRedisKey;
import com.ziyuan.shop.cloud.resp.MemberServerMsg;
import com.ziyuan.shop.cloud.service.IUserService;
import com.ziyuan.shop.cloud.util.JSONUtil;
import com.ziyuan.shop.cloud.util.MD5Util;
import com.ziyuan.shop.cloud.vo.LoginVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {

    private final UserMapper userMapper;
    private final StringRedisTemplate redisTemplate;

    public UserServiceImpl(UserMapper userMapper, StringRedisTemplate redisTemplate) {
        this.userMapper = userMapper;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public User findById(Long id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public String login(LoginVo loginVo) {
        if (StringUtils.isEmpty(loginVo.getUsername())) {
            throw new BusinessException(MemberServerMsg.PARAM_ERROR);
        }

        User user = this.findById(Long.parseLong(loginVo.getUsername()));
        if (user == null) {
            throw new BusinessException(MemberServerMsg.USERNAME_OR_PASSWORD_ERROR);
        }
        String inputPassword = MD5Util.formPass2DbPass(loginVo.getPassword(), user.getSalt());
        if (!inputPassword.equals(user.getPassword())) {
            throw new BusinessException(MemberServerMsg.USERNAME_OR_PASSWORD_ERROR);
        }
         token
        return createToken(user);
    }

    private String createToken(User user) {
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        try {
             redis
            String userStr = JSONUtil.toJSONString(user);
            redisTemplate.opsForValue().set(
                    MemberRedisKey.USER_TOKEN_KEY.join(token),
                    userStr,
                    MemberRedisKey.USER_TOKEN_KEY.getExpireTime(),
                    MemberRedisKey.USER_TOKEN_KEY.getUnit()); 
        } catch (Exception e) {
            e.printStackTrace();
        }
        return token;
    }

    @Override
    public User findByToken(String token) {
        String json = redisTemplate.opsForValue().get(MemberRedisKey.USER_TOKEN_KEY.join(token));
        if (!org.springframework.util.StringUtils.isEmpty(json)) {
            return JSONUtil.parseObject(json, User.class);
        }
        return null;
    }

    @Override
    public boolean refreshToken(String token) {
        Boolean expire = redisTemplate.expire(
                MemberRedisKey.USER_TOKEN_KEY.join(token),
                MemberRedisKey.USER_TOKEN_KEY.getExpireTime(),
                MemberRedisKey.USER_TOKEN_KEY.getUnit());
        return expire != null && expire;
    }
}
