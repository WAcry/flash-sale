package com.ziyuan.shop.cloud.web.feign;

import com.ziyuan.shop.cloud.resp.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("member-server")
public interface UserFeignApi {

    @RequestMapping("/refreshToken")
    Result<Boolean> refreshToken(@RequestParam("token") String token);
}
