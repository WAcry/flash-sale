package com.ziyuan.shop.cloud.mq.listener;

import com.ziyuan.shop.cloud.mq.MQConstants;
import com.ziyuan.shop.cloud.mq.msg.DelayOrderMsg;
import com.ziyuan.shop.cloud.service.IOrderInfoService;
import com.ziyuan.shop.cloud.util.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RocketMQMessageListener(
        topic = MQConstants.SECKILL_ORDER_TOPIC,
        selectorExpression = MQConstants.DELAY_ORDER_TAG,
        consumerGroup = MQConstants.DELAY_ORDER_CONSUMER_GROUP
)
public class DelayOrderMQListener implements RocketMQListener<DelayOrderMsg> {

    @Autowired
    private IOrderInfoService orderInfoService;

    @Override
    public void onMessage(DelayOrderMsg message) {
        orderInfoService.checkTimeout(message.getOrderNo(), message.getSeckillId());
    }
}
