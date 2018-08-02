package com.example.a13834598889.lovepets.JavaBean;

import android.content.Intent;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by 13834598889 on 2018/5/8.
 */

public class User extends BmobUser{
    private Integer age;
    private String signature;
    private BmobFile picture;
    private DianZan dianZan;
    private Pet pet;
    private Integer hadPet;
    private String nickName;

    private String im_id;
    private String im_token;
    private BmobRelation friends;
    private BmobRelation cards;
    private String data;


    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public BmobRelation getCards() {
        return cards;
    }

    public void setCards(BmobRelation cards) {
        this.cards = cards;
    }

    public BmobRelation getFriends() {
        return friends;
    }

    public void setFriends(BmobRelation friends) {
        this.friends = friends;
    }

    public String getIm_id() {
        return im_id;
    }

    public void setIm_id(String im_id) {
        this.im_id = im_id;
    }

    public String getIm_token() {
        return im_token;
    }

    public void setIm_token(String im_token) {
        this.im_token = im_token;
    }

    public User (){
        age = 0;
        signature = "";
        picture = null;
        dianZan = null;
        pet = null;
        hadPet = 0;
        nickName = "Century";
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getHadPet() {
        return hadPet;
    }

    public void setHadPet(Integer hadPet) {
        this.hadPet = hadPet;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public DianZan getDianZan() {
        return dianZan;
    }

    public void setDianZan(DianZan dianZan) {
        this.dianZan = dianZan;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public BmobFile getPicture() {
        return picture;
    }

    public void setPicture(BmobFile picture) {
        this.picture = picture;
    }
}
