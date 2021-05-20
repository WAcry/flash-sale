package com.ziyuan.shop.cloud.mq.listener;

import com.ziyuan.shop.cloud.core.WebSocketSessionManager;
import com.ziyuan.shop.cloud.mq.MQConstants;
import com.ziyuan.shop.cloud.mq.msg.SeckillSuccessMsg;
import com.ziyuan.shop.cloud.resp.Result;
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
        selectorExpression = MQConstants.SECKILL_SUCCESS_TAG,
        consumerGroup = MQConstants.SECKILL_SUCCESS_CONSUMER_GROUP
)
public class SeckillSuccessMQListener implements RocketMQListener<SeckillSuccessMsg> {

    @Override
    public void onMessage(SeckillSuccessMsg message) {

        Result<String> result = Result.success(message.getOrderNo());
        
        boolean ret;
        int count = 0, max = 3;
        do {
            
            ret = WebSocketSessionManager.INSTANCE.sendMsg(message.getUuid(), result);
            if (!ret) {
                try {
                    Thread.sleep(10L);
                } catch (Exception ignored) {
                }
            }

            count++;
        } while (!ret && count <= max);
    }
}
