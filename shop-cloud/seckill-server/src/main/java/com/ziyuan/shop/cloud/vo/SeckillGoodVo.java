package com.ziyuan.shop.cloud.vo;

import com.ziyuan.shop.cloud.domain.SeckillGood;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class SeckillGoodVo extends SeckillGood {

    private String goodName;
    private String goodTitle;
    private String goodImg;
    private String goodDetail;
    private BigDecimal goodPrice;
    private Integer goodStock;
}
