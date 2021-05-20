package com.ziyuan.shop.cloud.service;

import com.ziyuan.shop.cloud.domain.OrderInfo;
import com.ziyuan.shop.cloud.domain.User;

public interface IOrderInfoService {

    /**
     *
     * @param seckillId item id
     * @param userId
     * @return order id
     */
    String doSeckill(Long seckillId, Long userId);

    /**
     *
     * @param orderNo
     * @param userId
     * @return
     */
    OrderInfo findById(String orderNo, Long userId);

    /**
     * @param orderNo
     * @param seckillId
     */
    void checkTimeout(String orderNo, Long seckillId);

    /**
     * @param seckillId
     * @param userId
     */
    void seckillFailed(Long seckillId, Long userId);

    /**
     * @param orderNo
     */
    void updatePaySuccess(String orderNo);
}
