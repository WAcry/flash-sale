package com.ziyuan.shop.cloud.service.impl;

import com.ziyuan.shop.cloud.domain.Good;
import com.ziyuan.shop.cloud.domain.SeckillGood;
import com.ziyuan.shop.cloud.mapper.SeckillGoodMapper;
import com.ziyuan.shop.cloud.redis.key.SeckillRedisKey;
import com.ziyuan.shop.cloud.resp.Result;
import com.ziyuan.shop.cloud.service.ISeckillGoodService;
import com.ziyuan.shop.cloud.util.JSONUtil;
import com.ziyuan.shop.cloud.vo.SeckillGoodVo;
import com.ziyuan.shop.cloud.web.feign.GoodFeignApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
@Slf4j
public class SeckillGoodServiceImpl implements ISeckillGoodService {

    private final SeckillGoodMapper seckillGoodMapper;
    private final GoodFeignApi goodFeignApi;
    private final StringRedisTemplate redisTemplate;


    public SeckillGoodServiceImpl(SeckillGoodMapper seckillGoodMapper, GoodFeignApi goodFeignApi, StringRedisTemplate redisTemplate) {
        this.seckillGoodMapper = seckillGoodMapper;
        this.goodFeignApi = goodFeignApi;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List<SeckillGoodVo> query() {
        List<SeckillGood> seckillGoods = seckillGoodMapper.selectList();
        return join(seckillGoods);
    }

    @Override
    public SeckillGoodVo detail(Long seckillId) {
        SeckillGood seckillGood = seckillGoodMapper.selectByPrimaryKey(seckillId);
        List<SeckillGoodVo> voList = join(Collections.singletonList(seckillGood));
        return CollectionUtils.isEmpty(voList) ? null : voList.get(0);
    }

    public SeckillGoodVo detailByCache(Long seckillId) {
        String json = redisTemplate.opsForValue().get(SeckillRedisKey.SECKILL_GOODS_DETAIL.join(seckillId + ""));
        if (!StringUtils.isEmpty(json)) {
            return JSONUtil.parseObject(json, SeckillGoodVo.class);
        }

        return null;
    }

    @Override
    public int decrStockCount(Long seckillId) {
        return seckillGoodMapper.decrStockCount(seckillId);
    }

    @Override
    public SeckillGood findById(Long seckillId) {
        return seckillGoodMapper.selectByPrimaryKey(seckillId);
    }

    @Override
    public void incrStockCount(Long seckillId) {
        seckillGoodMapper.incrStockCount(seckillId);
    }

    private List<SeckillGoodVo> join(List<SeckillGood> seckillGoods) {
        Set<Long> idList = new HashSet<>(seckillGoods.size());
        for (SeckillGood seckillGood : seckillGoods) {
            idList.add(seckillGood.getGoodId());
        }

//        idList = seckillGoods.stream().map(SeckillGood::getId).collect(Collectors.toSet());
        Result<List<Good>> goodListResult = goodFeignApi.getListByIdList(idList);

        if (goodListResult == null || goodListResult.hasError()) {
            return null;
        }

        
        List<Good> goodList = goodListResult.getData();
        Map<Long, Good> tmpCache = new HashMap<>(goodList.size());
        for (Good good : goodList) {
            tmpCache.put(good.getId(), good);
        }

        List<SeckillGoodVo> voList = new ArrayList<>(seckillGoods.size());
        for (SeckillGood seckillGood : seckillGoods) {
            SeckillGoodVo vo = new SeckillGoodVo();

            
            BeanUtils.copyProperties(seckillGood, vo);

            Good good = tmpCache.get(seckillGood.getGoodId());
            if (good != null) {
                BeanUtils.copyProperties(good, vo);
            }

            vo.setId(seckillGood.getId());
            voList.add(vo);
        }

        return voList;
    }
}
