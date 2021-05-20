package com.ziyuan.shop.cloud.mq.msg;

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
public class SeckillSuccessMsg implements Serializable {

    private String uuid;
    private String orderNo;
}
