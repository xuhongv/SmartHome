package com.xuhong.smarthome.bean;

import cn.bmob.v3.BmobUser;

/*
 * 项目名：smarthome
 * 包名：com.xuhong.smarthome.bean.User
 * 创建时间：2017/8/12  下午 5:50
 * 创建者  xuhong
 * 描述：User用户类
 */

public class User extends BmobUser {

    //昵称
    private String name;

    //性别 1表示男 、2表示女 、3未知
    private int sex;

    //个性签名
    private String remark;

    //头像
    private String pic;

    //云端唯一的用户标志
    private String token;

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }


}
