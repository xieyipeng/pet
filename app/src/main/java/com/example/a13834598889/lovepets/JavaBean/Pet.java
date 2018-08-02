package com.example.a13834598889.lovepets.JavaBean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by 13834598889 on 2018/5/8.
 */

public class Pet extends BmobObject{
    private String name;
    private String sex;
    private int age;
    private BmobFile picture;
    private User user;
    private Integer isFQQ;
    private String zhongLei;


    public Pet(){
        this.name = "";
        this.sex = "";
        this.age = 0;
        this.isFQQ = 0;
        this.picture  = null;
        this.user = null;
    }

    public String getZhongLei() {
        return zhongLei;
    }

    public void setZhongLei(String zhongLei) {
        this.zhongLei = zhongLei;
    }

    public Integer getIsFQQ() {
        return isFQQ;
    }

    public void setIsFQQ(Integer isFQQ) {
        this.isFQQ = isFQQ;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public BmobFile getPicture() {
        return picture;
    }

    public void setPicture(BmobFile picture) {
        this.picture = picture;
    }
}
