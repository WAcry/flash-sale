package com.ziyuan.shop.cloud.web.controller;

import com.ziyuan.shop.cloud.domain.User;
import com.ziyuan.shop.cloud.exception.BusinessException;
import com.ziyuan.shop.cloud.redis.key.SeckillRedisKey;
import com.ziyuan.shop.cloud.util.CookieUtil;
import com.ziyuan.shop.cloud.util.VerifyCodeImgUtil;
import com.ziyuan.shop.cloud.web.SeckillCodeMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Controller
@RequestMapping("/api/verify/code")
public class VerifyCodeController extends BaseController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping
    public void generate(Long seckillId, @CookieValue(CookieUtil.TOKEN_IN_COOKIE) String userToken, HttpServletResponse response) throws IOException {
        User user = getCurrentUser(userToken);
        if (user == null || seckillId == null) {
            throw new BusinessException(SeckillCodeMsg.OP_ERROR);
        }

        String exp = VerifyCodeImgUtil.generateVerifyCode();
        Integer ret = VerifyCodeImgUtil.calc(exp);
        redisTemplate.opsForValue().set(
                SeckillRedisKey.SECKILL_VERIFY_CODE.join(seckillId+"", user.getId()+""),
                ret + "",
                SeckillRedisKey.SECKILL_VERIFY_CODE.getExpireTime(),
                SeckillRedisKey.SECKILL_VERIFY_CODE.getUnit());

        BufferedImage verifyCodeImg = VerifyCodeImgUtil.createVerifyCodeImg(exp);
        response.setContentType("image/jpg");
        ImageIO.write(verifyCodeImg, "JPEG", response.getOutputStream());
    }
}
