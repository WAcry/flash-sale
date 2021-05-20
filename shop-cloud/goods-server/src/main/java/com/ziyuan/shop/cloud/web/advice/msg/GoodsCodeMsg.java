package com.ziyuan.shop.cloud.web.advice.msg;


import com.ziyuan.shop.cloud.resp.CodeMsg;

public class GoodsCodeMsg extends CodeMsg {
    private GoodsCodeMsg(String code, String msg){
        super(code,msg);
    }
    public static final GoodsCodeMsg DEFAULT_ERROR = new GoodsCodeMsg("B002001","Busying");

}
