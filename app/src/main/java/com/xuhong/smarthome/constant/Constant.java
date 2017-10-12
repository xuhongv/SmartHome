package com.xuhong.smarthome.constant;

/**
 * 项目名： SmartHome
 * 包名： com.xuhong.smarthome.Constant
 * 文件名字： Constant
 * 创建时间：2017/8/7 17:40
 * 项目名： Xuhong
 * 描述： 常量类
 */

public class Constant {


    /**
     * 正则：邮箱
     */
    public static final String REGEX_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";

    //极速数据的AppKey
    public static String JUSU_APPKEY = "852f41031d9f70b8";

    //bmob的application ID ：初始化用
    public static String BMOB_APPLICATION_ID = "a565f453d477f3b17fc4a1aba65adcc5";

    //获取新闻频道
    public static String URL_GET_NEWS_CHANNEL = "http://api.jisuapi.com/news/channel?appkey=" + JUSU_APPKEY;

    //版本获取
    public static String GET_APP_VERSION = "http://smarthomesample.oss-cn-hongkong.aliyuncs.com/appVersion.txt";














    /** 存储器默认名称 */
    public static final String SPF_Name = "set";

    //wifi名字
    public static final String WIFI_NAME ="wifiName";

    //wifi密码
    public static final String WIFI_PW = "wifiPw";

    /** 设备热点默认密码 */
    public static final String SoftAP_PSW = "123456789";

    /** 设备热点默认前缀 */
    public static final String SoftAP_Start = "XPG-GAgent";

}
