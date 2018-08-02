package com.example.a13834598889.lovepets.JavaBean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by 13834598889 on 2018/5/8.
 */

public class Record extends BmobObject{
    private String title;
    private String context;
    private String time;
    private User user;
    private BmobFile image_back;

    public BmobFile getImage_back() {
        return image_back;
    }

    public void setImage_back(BmobFile image_back) {
        this.image_back = image_back;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
