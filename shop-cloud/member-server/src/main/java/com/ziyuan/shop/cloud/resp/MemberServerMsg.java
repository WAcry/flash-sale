package com.ziyuan.shop.cloud.resp;

public class MemberServerMsg extends CodeMsg {

    public static final MemberServerMsg USERNAME_OR_PASSWORD_ERROR = new MemberServerMsg("500401", "incorrect usrname or password");

    private MemberServerMsg(String code, String msg) {
        super(code, msg);
    }
}
