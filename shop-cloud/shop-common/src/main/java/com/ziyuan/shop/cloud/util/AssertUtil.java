package com.ziyuan.shop.cloud.util;

import com.ziyuan.shop.cloud.exception.BusinessException;
import com.ziyuan.shop.cloud.resp.CodeMsg;
import org.springframework.util.StringUtils;

/**
 *
 */
public class AssertUtil {

    /**
     *
     * @param value
     * @param codeMsg
     */
    public static void notNull(Object value, CodeMsg codeMsg) {
        if (value == null) {
            throw new BusinessException(codeMsg);
        }
    }

    /**
     * @param value
     * @param codeMsg
     * @return
     */
    public static void hasLength(String value, CodeMsg codeMsg) {
        if(!StringUtils.hasLength(value)){
            throw new BusinessException(codeMsg);
        }
    }

    /**
     * @param isTrue
     * @param codeMsg
     */
    public static void isTrue(boolean isTrue, CodeMsg codeMsg) {
        if (!isTrue) {
            throw new BusinessException(codeMsg);
        }
    }
}
