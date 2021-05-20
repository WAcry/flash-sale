package com.ziyuan.shop.cloud.mq;

import com.ziyuan.shop.cloud.util.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;

@Slf4j
public class MQLogSendCallback implements SendCallback {

    @Override
    public void onSuccess(SendResult sendResult) {
        log.info("[SendCallback] successfully sent message:{}", JSONUtil.toJSONString(sendResult));
    }

    @Override
    public void onException(Throwable throwable) {
        log.error("[SendCallback] fail to send message！", throwable);
    }
}
