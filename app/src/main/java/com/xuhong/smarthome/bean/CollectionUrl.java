package com.xuhong.smarthome.bean;

import cn.bmob.v3.BmobObject;


public class CollectionUrl extends BmobObject {

    private String uri;

    private User user;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
