package com.ziyuan.shop.cloud.mq.listener;

import com.ziyuan.shop.cloud.core.WebSocketSessionManager;
import com.ziyuan.shop.cloud.mq.MQConstants;
import com.ziyuan.shop.cloud.mq.msg.SeckillFailedMsg;
import com.ziyuan.shop.cloud.resp.Result;
import com.ziyuan.shop.cloud.util.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RocketMQMessageListener(
        topic = MQConstants.SECKILL_ORDER_TOPIC,
        selectorExpression = MQConstants.SECKILL_FAILED_TAG,
        consumerGroup = MQConstants.SECKILL_FAILED_CONSUMER_GROUP
)
public class SeckillFailedMQListener implements RocketMQListener<SeckillFailedMsg> {

    @Override
    public void onMessage(SeckillFailedMsg message) {

        Result<?> result = Result.error(message.getCodeMsg());
        
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
