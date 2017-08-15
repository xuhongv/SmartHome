package com.xuhong.smarthome.bean;



/*
 * 项目名：smarthome
 * 包名：com.xuhong.smarthome.bean.User
 * 创建时间：2017/8/12  下午 5:50
 * 创建者  xuhong
 * 描述：User用户类
 */

import cn.bmob.v3.BmobUser;

public class User extends BmobUser {


    //头像
    private String nick;


    public void setText(String text) {

        this.text = text;
    }

    private String text;



    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }


}
