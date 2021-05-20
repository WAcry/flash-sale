package com.ziyuan.shop.cloud.resp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CodeMsg implements Serializable {

    private String code;
    private String msg;

    public static final CodeMsg PARAM_ERROR = new CodeMsg("A0002", "incorrect paramater");
    public static final CodeMsg DEFAULT_ERROR = new CodeMsg("A0000", "server error");
    public static final CodeMsg RATE_LIMIT_ERROR = new CodeMsg("A0003", "too many requests");
}
