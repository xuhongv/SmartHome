package com.xuhong.smarthome.bean;

/**
 * Created by Administrator on 2017/8/8 0008.
 */

public class HomeNewsListItemBean {


    private String title;
    private String creatTime;
    private String picUrl;

    public String getNewsFrom() {
        return newsFrom;
    }

    private String newsFrom;

    public String getTitle() {
        return title;
    }

    public String getCreatTime() {
        return creatTime;
    }

    public String getPicUrl() {
        return picUrl;
    }


    public HomeNewsListItemBean(String title, String creatTime, String picUrl, String newsFrom) {
        this.title = title;
        this.creatTime = creatTime;
        this.picUrl = picUrl;
        this.newsFrom = newsFrom;
    }


}
