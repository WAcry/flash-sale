package com.ziyuan.shop.cloud.service;

import com.ziyuan.shop.cloud.domain.SeckillOrder;

public interface ISeckillOrderService {
    /**
     *
     * @param userId
     * @param seckillId
     * @return
     */
    SeckillOrder findByUserIdAndSeckillId(Long userId, Long seckillId);

    /**
     *
     * @param seckillId
     * @param userId
     * @param orderNo
     */
    void createSeckillOrder(Long seckillId, Long userId, String orderNo);
}
