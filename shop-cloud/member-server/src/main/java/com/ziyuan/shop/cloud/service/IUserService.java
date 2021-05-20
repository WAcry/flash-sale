package com.ziyuan.shop.cloud.service;

import com.ziyuan.shop.cloud.domain.User;
import com.ziyuan.shop.cloud.vo.LoginVo;

public interface IUserService {

    /**
     * find user by id
     * @param id
     * @return
     */
    User findById(Long id);

    /**
     * login
     * @param vo
     */
    String login(LoginVo vo);

    /**
     *find user by token in redis
     * @param token
     * @return
     */
    User findByToken(String token);

    /**
     *
     * @param token
     * @return
     */
    boolean refreshToken(String token);
}
