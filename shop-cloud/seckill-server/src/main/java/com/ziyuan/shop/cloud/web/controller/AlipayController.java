package com.ziyuan.shop.cloud.web.controller;

import com.alipay.api.AlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.ziyuan.shop.cloud.alipay.config.AlipayProperties;
import com.ziyuan.shop.cloud.domain.OrderInfo;
import com.ziyuan.shop.cloud.domain.User;
import com.ziyuan.shop.cloud.exception.BusinessException;
import com.ziyuan.shop.cloud.service.IOrderInfoService;
import com.ziyuan.shop.cloud.util.CookieUtil;
import com.ziyuan.shop.cloud.web.SeckillCodeMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author Leon
 * @date 2020/8/23
 */
@RequestMapping("/api/alipay")
@RestController
public class AlipayController extends BaseController {

    @Autowired
    private AlipayProperties properties;

    @Autowired
    private AlipayClient alipayClient;

    @Autowired
    private IOrderInfoService orderInfoService;

    @RequestMapping("/pay")
    public void pay(String orderNo, @CookieValue(CookieUtil.TOKEN_IN_COOKIE) String token, HttpServletResponse resp) throws Exception {
        User user = this.getCurrentUser(token);
        if (user == null) {
            throw new BusinessException(SeckillCodeMsg.OP_ERROR);
        }

        
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(properties.getReturnUrl());
        alipayRequest.setNotifyUrl(properties.getNotifyUrl());

        
        OrderInfo order = orderInfoService.findById(orderNo, user.getId());
        if (order == null) {
            throw new BusinessException(SeckillCodeMsg.OP_ERROR);
        }

        String out_trade_no = orderNo;
        String total_amount = order.getSeckillPrice() + "";
        String subject = order.getGoodName();
        String body = "";

        alipayRequest.setBizContent("{\"out_trade_no\":\"" + out_trade_no + "\","
                + "\"total_amount\":\"" + total_amount + "\","
                + "\"subject\":\"" + subject + "\","
                + "\"body\":\"" + body + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        String result = alipayClient.pageExecute(alipayRequest).getBody();
        System.out.println(result);
        // html : -> <form> <script>document.getElementByTag("form")[0].submit;
        resp.setContentType("text/html;charset=utf-8");

        resp.getWriter().println(result);
    }

    /**
     */
    @RequestMapping("/returnUrl")
    public void returnUrl(@RequestParam Map<String, String> params, HttpServletResponse resp) throws Exception {
        boolean signVerified = AlipaySignature.rsaCheckV2(params, properties.getAlipayPublicKey(),
                properties.getCharset(), properties.getSignType());

        if(signVerified) {
            String out_trade_no = params.get("out_trade_no");

            String trade_no = params.get("trade_no");

            String total_amount = params.get("total_amount");

            resp.sendRedirect("http://localhost/order_detail.html?orderNo="+out_trade_no);
        }else {
            
            resp.sendRedirect("https://baidu.com");
        }
    }

    /**
     */
    @RequestMapping("/notifyUrl")
    public String notifyUrl(@RequestParam Map<String, String> params) throws Exception {
        boolean signVerified = AlipaySignature.rsaCheckV1(params, properties.getAlipayPublicKey(),
                properties.getCharset(), properties.getSignType());

        if(signVerified) {
            String out_trade_no = params.get("out_trade_no");
            String trade_no = params.get("trade_no");
            String trade_status = params.get("trade_status");
            if(trade_status.equals("TRADE_FINISHED")){
            }else if (trade_status.equals("TRADE_SUCCESS")){
                orderInfoService.updatePaySuccess(out_trade_no);
            }

            return "success";
        }

        return "fail";
    }
}
