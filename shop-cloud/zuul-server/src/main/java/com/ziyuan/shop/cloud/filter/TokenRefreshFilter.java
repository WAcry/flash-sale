package com.ziyuan.shop.cloud.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.ziyuan.shop.cloud.resp.Result;
import com.ziyuan.shop.cloud.util.CookieUtil;
import com.ziyuan.shop.cloud.web.feign.UserFeignApi;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class TokenRefreshFilter extends ZuulFilter {

    @Autowired
    private UserFeignApi userFeignApi;

    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest req = ctx.getRequest();
        String token = CookieUtil.getCookieValue(CookieUtil.TOKEN_IN_COOKIE, req);
        return !StringUtils.isEmpty(token);
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest req = ctx.getRequest();
        String token = CookieUtil.getCookieValue(CookieUtil.TOKEN_IN_COOKIE, req);
        Result<Boolean> result = userFeignApi.refreshToken(token);
        if (result == null || result.hasError()) {
            return null;
        }

        Boolean refreshed = result.getData();
        if (refreshed) {
            CookieUtil.addCookie(CookieUtil.TOKEN_IN_COOKIE, token, ctx.getResponse());
        }
        return null;
    }
}
