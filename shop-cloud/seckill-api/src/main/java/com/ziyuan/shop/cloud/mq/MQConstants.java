package com.ziyuan.shop.cloud.mq;

/**
 *mq
 */
public interface MQConstants {

    /**
     */
    String SECKILL_ORDER_TOPIC = "SECKILL_ORDER_TOPIC";

    /**
     */
    String CREATE_ORDER_TAG = "CREATE_ORDER_TAG";
    /**
     */
    String CREATE_ORDER_DEST = SECKILL_ORDER_TOPIC + ":" + CREATE_ORDER_TAG;
    /**
     */
    String CREATE_ORDER_CONSUMER_GROUP = "CREATE_ORDER_CONSUMER_GROUP";

    /**
     */
    String SECKILL_SUCCESS_TAG = "SECKILL_SUCCESS_TAG";
    /**
     */
    String SECKILL_SUCCESS_DEST = SECKILL_ORDER_TOPIC + ":" + SECKILL_SUCCESS_TAG;
    /**
     */
    String SECKILL_SUCCESS_CONSUMER_GROUP = "SECKILL_SUCCESS_CONSUMER_GROUP";

    /**
     */
    String SECKILL_FAILED_TAG = "SECKILL_FAILED_TAG";
    /**
     */
    String SECKILL_FAILED_DEST = SECKILL_ORDER_TOPIC + ":" + SECKILL_FAILED_TAG;
    /**
     */
    String SECKILL_FAILED_CONSUMER_GROUP = "SECKILL_FAILED_CONSUMER_GROUP";

    /**
     */
    String DELAY_ORDER_TAG = "DELAY_ORDER_TAG";
    /**
     */
    String DELAY_ORDER_DEST = SECKILL_ORDER_TOPIC + ":" + DELAY_ORDER_TAG;
    /**
     */
    String DELAY_ORDER_CONSUMER_GROUP = "DELAY_ORDER_CONSUMER_GROUP";

    /**
     */
    String CLEAR_STOCK_FLAG_TAG = "CLEAR_STOCK_FLAG_TAG";
    /**
     */
    String CLEAR_STOCK_FLAG_DEST = SECKILL_ORDER_TOPIC + ":" + CLEAR_STOCK_FLAG_TAG;
    /**
     */
    String CLEAR_STOCK_FLAG_CONSUMER_GROUP = "CLEAR_STOCK_FLAG_CONSUMER_GROUP";
}
