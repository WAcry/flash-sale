package com.ziyuan.shop.cloud.web.controller;

import com.ziyuan.shop.cloud.redis.key.SeckillRedisKey;
import com.ziyuan.shop.cloud.resp.Result;
import com.ziyuan.shop.cloud.service.ISeckillGoodService;
import com.ziyuan.shop.cloud.util.JSONUtil;
import com.ziyuan.shop.cloud.vo.SeckillGoodVo;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/seckill/goods")
public class SeckillGoodController {

    private final ISeckillGoodService seckillGoodService;
    private final StringRedisTemplate redisTemplate;

    public SeckillGoodController(ISeckillGoodService seckillGoodService, StringRedisTemplate redisTemplate) {
        this.seckillGoodService = seckillGoodService;
        this.redisTemplate = redisTemplate;
    }

    /**
     *：
     * 100 * 50
     * 1307.5/s
     * @return
     */
    @GetMapping
    public Result<List<SeckillGoodVo>> query() {
        List<SeckillGoodVo> list = seckillGoodService.query();
        return Result.success(list);
    }

    @GetMapping("/{seckillId}")
    public Result<SeckillGoodVo> findById(@PathVariable("seckillId") Long seckillId) {
        return Result.success(seckillGoodService.detail(seckillId));
    }

    @GetMapping("/initData")
    public Result<String> init() {
        
        List<SeckillGoodVo> query = seckillGoodService.query();
        for (SeckillGoodVo vo : query) {
            
            redisTemplate.opsForValue().set(
                    SeckillRedisKey.SECKILL_GOODS_DETAIL.join(vo.getId() + ""),
                    JSONUtil.toJSONString(vo),
                    SeckillRedisKey.SECKILL_GOODS_DETAIL.getExpireTime(),
                    SeckillRedisKey.SECKILL_GOODS_DETAIL.getUnit()
            );
            redisTemplate.opsForHash().put(
                    SeckillRedisKey.SECKILL_STOCK_COUNT_HASH.join(""),
                    vo.getId() + "",
                    vo.getStockCount() + ""
            );
        }
        return Result.success("success");
    }
}
