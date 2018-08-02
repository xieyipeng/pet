package com.example.a13834598889.lovepets.JavaBean;

import cn.bmob.v3.BmobObject;

/**
 * Created by 13834598889 on 2018/5/11.
 */

public class Msg extends BmobObject{

    public static final int TYPE_RECEIVED = 0;

    public static final int TYPE_SENT = 1;

    private String content;

    private int type;

    public Msg(String content, int type) {
        this.content = content;
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public int getType() {
        return type;
    }

}