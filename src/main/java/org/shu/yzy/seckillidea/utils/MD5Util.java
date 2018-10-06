package org.shu.yzy.seckillidea.utils;

import org.springframework.util.DigestUtils;

public class MD5Util {
    public static String getMD5(String string){
        return DigestUtils.md5DigestAsHex(string.getBytes());
    }

    public static String getMD5(String id, String salt){
        return getMD5(id + salt);
    }
}
