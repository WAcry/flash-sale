package com.ziyuan.shop.cloud.web;

import com.ziyuan.shop.cloud.resp.CodeMsg;

public class SeckillCodeMsg extends CodeMsg {

    private SeckillCodeMsg(String code, String msg) {
        super(code, msg);
    }

    public static final SeckillCodeMsg OP_ERROR = new SeckillCodeMsg("A003001", "fail to get deal");
    public static final SeckillCodeMsg NOT_START_ERROR = new SeckillCodeMsg("A003002", "flash sale is not stareted");
    public static final SeckillCodeMsg END_ERROR = new SeckillCodeMsg("A003003", "flash sale is ended");
    public static final SeckillCodeMsg REPATE_ERROR = new SeckillCodeMsg("A003004", "do not buy twice");
    public static final SeckillCodeMsg VERIFY_CODE_ERROR = new SeckillCodeMsg("A003005", "wrong captcha");

    public static final CodeMsg DEFAULT_ERROR = new CodeMsg("B003001","system busy");
    public static final SeckillCodeMsg OUT_OF_STOCK_ERROR = new SeckillCodeMsg("B003001", "no item left");
}
