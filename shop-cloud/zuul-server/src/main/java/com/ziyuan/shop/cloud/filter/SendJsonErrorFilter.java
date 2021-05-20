package com.ziyuan.shop.cloud.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.ziyuan.shop.cloud.resp.CodeMsg;
import com.ziyuan.shop.cloud.resp.Result;
import com.ziyuan.shop.cloud.util.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.ERROR_TYPE;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SEND_ERROR_FILTER_ORDER;

@Slf4j
@Component
public class SendJsonErrorFilter extends ZuulFilter {

    protected static final String SEND_ERROR_FILTER_RAN = "sendErrorFilter.ran";

    @Override
    public String filterType() {
        return ERROR_TYPE;
    }

    @Override
    public int filterOrder() {
        return SEND_ERROR_FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        // only forward to errorPath if it hasn't been forwarded to already
        return ctx.getThrowable() != null
                && !ctx.getBoolean(SEND_ERROR_FILTER_RAN, false);
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        Throwable throwable = ctx.getThrowable();
        log.error("error handle:", throwable);

        Result<?> result = Result.error(CodeMsg.DEFAULT_ERROR);
        Object rateLimitExceeded = ctx.get("rateLimitExceeded");

        if (Boolean.TRUE.toString().equalsIgnoreCase(rateLimitExceeded + "")) {
            
            result = Result.error(CodeMsg.RATE_LIMIT_ERROR);
        }

        
        HttpServletResponse response = ctx.getResponse();
        response.setContentType("application/json;charset=utf-8");
        try {
            response.getWriter().write(JSONUtil.toJSONString(result));
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }
}
