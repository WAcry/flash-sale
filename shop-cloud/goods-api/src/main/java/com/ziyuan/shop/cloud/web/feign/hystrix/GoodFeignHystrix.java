package com.ziyuan.shop.cloud.web.feign.hystrix;

import com.ziyuan.shop.cloud.domain.Good;
import com.ziyuan.shop.cloud.resp.Result;
import com.ziyuan.shop.cloud.web.feign.GoodFeignApi;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class GoodFeignHystrix implements GoodFeignApi {

    @Override
    public Result<List<Good>> getListByIdList(Set<Long> idList) {
        return null;
    }
}
