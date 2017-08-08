package com.xuhong.smarthome.utils;

import com.alibaba.fastjson.JSON;
import com.xuhong.smarthome.bean.HomeNewListBean;
import com.xuhong.smarthome.bean.HomeNewsChannelBean;

/**
 * Created by admin on 2017/8/7.
 */

public class ParseJson {

    //首页新闻列表
    public static HomeNewListBean getHomeNewsListBean(String json, Class<HomeNewListBean> beanClass) {
        return JSON.parseObject(json, beanClass);
    }

    //首页新闻频道的json解析
    public static HomeNewsChannelBean getHomeNewsChannelBean(String json, Class<HomeNewsChannelBean> beanClass) {
        return JSON.parseObject(json, beanClass);
    }
}
