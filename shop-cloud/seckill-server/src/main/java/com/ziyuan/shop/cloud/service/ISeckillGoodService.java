package com.ziyuan.shop.cloud.service;

import com.ziyuan.shop.cloud.vo.SeckillGoodVo;
import com.ziyuan.shop.cloud.domain.SeckillGood;

import java.util.List;

public interface ISeckillGoodService {

    /**
     *
     * @return
     */
    List<SeckillGoodVo> query();


    /**
     *
     * @param seckillId
     * @return
     */
    SeckillGoodVo detail(Long seckillId);

    /**
     * @param seckillId
     * @return
     */
    SeckillGoodVo detailByCache(Long seckillId);

    int decrStockCount(Long seckillId);

    /**
     * @param seckillId
     * @return
     */
    SeckillGood findById(Long seckillId);

    /**
     * @param seckillId
     */
    void incrStockCount(Long seckillId);
}
