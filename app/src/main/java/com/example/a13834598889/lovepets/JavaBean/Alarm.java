package com.example.a13834598889.lovepets.JavaBean;

import cn.bmob.v3.BmobObject;

/**
 * Created by 13834598889 on 2018/5/15.
 */

public class Alarm extends BmobObject{
    private String time;
    private String text;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
