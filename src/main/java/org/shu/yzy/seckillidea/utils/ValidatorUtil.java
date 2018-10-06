package org.shu.yzy.seckillidea.utils;

import com.mysql.jdbc.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatorUtil {

    private static final Pattern mobilePattern = Pattern.compile("1\\d{10}");

    public static boolean isMobile(String mobile){
        if(StringUtils.isEmptyOrWhitespaceOnly(mobile)){
            return false;
        }
        Matcher matcher = mobilePattern.matcher(mobile);
        return matcher.matches();
    }
}
