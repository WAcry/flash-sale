package com.ziyuan.shop.cloud.vo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@Getter
@Setter
public class LoginVo {

    @Pattern(regexp = "^1[3|4|5|7|8][0-9]{9}$", message = "incorrect format")
    private String username;
    private String password;
}
