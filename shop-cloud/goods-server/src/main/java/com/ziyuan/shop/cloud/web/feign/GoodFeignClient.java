package com.ziyuan.shop.cloud.web.feign;

import com.ziyuan.shop.cloud.service.IGoodService;
import com.ziyuan.shop.cloud.domain.Good;
import com.ziyuan.shop.cloud.resp.Result;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
public class GoodFeignClient implements GoodFeignApi {

    private final IGoodService goodService;

    public GoodFeignClient(IGoodService goodService) {
        this.goodService = goodService;
    }

    @Override
    public Result<List<Good>> getListByIdList(Set<Long> idList) {
        return Result.success(goodService.getListByIdList(idList));
    }
}
