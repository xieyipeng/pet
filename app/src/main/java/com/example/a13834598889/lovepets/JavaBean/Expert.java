package com.example.a13834598889.lovepets.JavaBean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by 13834598889 on 2018/5/13.
 */

public class Expert extends BmobObject{
    private String title;
    private String text;
    private String zhangshu;
    private String zuoyou;
    private String daxiao;
    private BmobFile tu1;
    private BmobFile tu2;
    private BmobFile tu3;
    private String uri;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getZhangshu() {
        return zhangshu;
    }

    public void setZhangshu(String zhangshu) {
        this.zhangshu = zhangshu;
    }

    public String getZuoyou() {
        return zuoyou;
    }

    public void setZuoyou(String zuoyou) {
        this.zuoyou = zuoyou;
    }

    public String getDaxiao() {
        return daxiao;
    }

    public void setDaxiao(String daxiao) {
        this.daxiao = daxiao;
    }

    public BmobFile getTu1() {
        return tu1;
    }

    public void setTu1(BmobFile tu1) {
        this.tu1 = tu1;
    }

    public BmobFile getTu2() {
        return tu2;
    }

    public void setTu2(BmobFile tu2) {
        this.tu2 = tu2;
    }

    public BmobFile getTu3() {
        return tu3;
    }

    public void setTu3(BmobFile tu3) {
        this.tu3 = tu3;
    }
}
