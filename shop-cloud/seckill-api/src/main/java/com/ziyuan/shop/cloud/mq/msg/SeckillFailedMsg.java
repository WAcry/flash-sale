package com.ziyuan.shop.cloud.mq.msg;

import com.ziyuan.shop.cloud.resp.CodeMsg;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SeckillFailedMsg implements Serializable {

    private String uuid;
    private CodeMsg codeMsg;
}
