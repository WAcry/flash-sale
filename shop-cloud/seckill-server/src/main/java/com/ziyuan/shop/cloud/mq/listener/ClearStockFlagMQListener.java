package com.ziyuan.shop.cloud.mq.listener;

import com.ziyuan.shop.cloud.mq.MQConstants;
import com.ziyuan.shop.cloud.web.controller.OrderController;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RocketMQMessageListener(
        topic = MQConstants.SECKILL_ORDER_TOPIC,
        selectorExpression = MQConstants.CLEAR_STOCK_FLAG_TAG,
        messageModel = MessageModel.BROADCASTING,
        consumerGroup = MQConstants.CLEAR_STOCK_FLAG_CONSUMER_GROUP
)
public class ClearStockFlagMQListener implements RocketMQListener<Long> {

    @Override
    public void onMessage(Long seckillId) {
        OrderController.STOCK_COUNT_FLAG.put(seckillId, false);
    }
}
