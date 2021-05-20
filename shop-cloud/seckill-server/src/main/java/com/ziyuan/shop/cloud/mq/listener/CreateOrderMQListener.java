package com.ziyuan.shop.cloud.mq.listener;

import com.ziyuan.shop.cloud.exception.BusinessException;
import com.ziyuan.shop.cloud.mq.MQConstants;
import com.ziyuan.shop.cloud.mq.MQLogSendCallback;
import com.ziyuan.shop.cloud.mq.msg.CreateSeckillOrderMsg;
import com.ziyuan.shop.cloud.mq.msg.DelayOrderMsg;
import com.ziyuan.shop.cloud.mq.msg.SeckillFailedMsg;
import com.ziyuan.shop.cloud.mq.msg.SeckillSuccessMsg;
import com.ziyuan.shop.cloud.resp.CodeMsg;
import com.ziyuan.shop.cloud.service.IOrderInfoService;
import com.ziyuan.shop.cloud.util.JSONUtil;
import com.ziyuan.shop.cloud.web.SeckillCodeMsg;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RocketMQMessageListener(
        topic = MQConstants.SECKILL_ORDER_TOPIC,
        selectorExpression = MQConstants.CREATE_ORDER_TAG,
        consumerGroup = MQConstants.CREATE_ORDER_CONSUMER_GROUP
)
public class CreateOrderMQListener implements RocketMQListener<CreateSeckillOrderMsg> {

    @Autowired
    private IOrderInfoService orderInfoService;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Override
    public void onMessage(CreateSeckillOrderMsg message) {
        log.info("[create order consumer] receive order creation msg:{}", JSONUtil.toJSONString(message));

        try {
            
            String orderNo = orderInfoService.doSeckill(message.getSeckillId(), message.getUserId());
            try {
                rocketMQTemplate.asyncSend(MQConstants.SECKILL_SUCCESS_DEST, new SeckillSuccessMsg(message.getUuid(), orderNo), new MQLogSendCallback());
                rocketMQTemplate.asyncSend(MQConstants.DELAY_ORDER_DEST, new GenericMessage<>(new DelayOrderMsg(orderNo, message.getSeckillId())),
                        new MQLogSendCallback(), 1000, 3);
            } catch (Exception e) {
            }
        } catch (Exception e) {
            CodeMsg codeMsg = SeckillCodeMsg.OP_ERROR;
            if (e instanceof BusinessException) {
                BusinessException be = (BusinessException) e;
                codeMsg = be.getCodeMsg();
            }
            rocketMQTemplate.asyncSend(MQConstants.SECKILL_FAILED_DEST, new SeckillFailedMsg(message.getUuid(), codeMsg), new MQLogSendCallback());
            orderInfoService.seckillFailed(message.getSeckillId(), message.getUserId());
        }
    }
}
