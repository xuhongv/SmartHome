package com.xuhong.smarthome.utils;

import com.xuhong.smarthome.constant.Constant;

import java.util.regex.Pattern;

/**
 * 项目名： SmartHome-master
 * 包名： com.xuhong.smarthome.utils
 * 文件名字： RegexUtils
 * 创建时间：2017/8/15 20:18
 * 项目名： Xuhong
 * 描述： 正则表达式
 */

public class RegexUtils {


    private RegexUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }


    /**
     * 验证邮箱
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isEmail(final CharSequence input) {
        return isMatch(Constant.REGEX_EMAIL, input);
    }

    /**
     * 判断是否匹配正则
     *
     * @param regex 正则表达式
     * @param input 要匹配的字符串
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    private static boolean isMatch(final String regex, final CharSequence input) {
        return input != null && input.length() > 0 && Pattern.matches(regex, input);
    }
}
