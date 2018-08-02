package com.example.a13834598889.lovepets.JavaBean;

import android.content.Intent;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by 13834598889 on 2018/5/7.
 */

public class Card extends BmobObject{

    private User user;
    private String title;
    private String context;
    private String time;
    private BmobFile picture;
    private Integer isJScard;
    private Integer num_dianzan;

    public Card (){
        this.isJScard = 0;
        this.num_dianzan = 0;
    }

    public Integer getNum_dianzan() {
        return num_dianzan;
    }

    public void setNum_dianzan(Integer num_dianzan) {
        this.num_dianzan = num_dianzan;
    }

    public Integer getIsJScard() {
        return isJScard;
    }

    public void setIsJScard(Integer isJScard) {
        this.isJScard = isJScard;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public BmobFile getPicture() {
        return picture;
    }

    public void setPicture(BmobFile picture) {
        this.picture = picture;
    }
}
