package com.ziyuan.shop.cloud.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * MD5
 */
public class MD5Util {

    private final static String SALT = "1a2c3d4e";

    /**
     *
     * @param password
     * @return
     */
    public static String inputPass2FormPass(String password) {
        // 111111  -> 1211111134
        String str = "" + SALT.charAt(0) + SALT.charAt(2) + password + SALT.charAt(4) + SALT.charAt(6);
        return DigestUtils.md5Hex(str);
    }

    /**
     *
     * @param password
     * @param salt
     * @return
     */
    public static String formPass2DbPass(String password, String salt) {
        // 111111  -> 1211111134
        String str = "" + salt.charAt(0) + salt.charAt(2) + password + salt.charAt(4) + salt.charAt(6);
        System.out.println(str);
        return DigestUtils.md5Hex(str);
    }


    public static void main(String[] args) {
        // 111111 -> 1211111134 -> 8efd0d84097291edffe1e7774e4344de -> 128efd0d84097291edffe1e7774e4344de34 -> 027c528110bbd7cd4b93ebc661cb7dd7
        String input = "111111";
        String formPass = inputPass2FormPass(input);
        System.out.println(formPass);
        String dbPass = formPass2DbPass(formPass, SALT);
        System.out.println(dbPass);
    }
}
