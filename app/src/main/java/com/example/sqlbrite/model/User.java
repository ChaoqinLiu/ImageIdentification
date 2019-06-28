package com.example.sqlbrite.model;

import com.google.gson.annotations.SerializedName;

public class User {
    String nickname;

    @SerializedName("figureurl_qq_1")
    String figure;

    public String getNickname() {
        return nickname;
    }

    public String getFigure() {
        return figure;
    }
}
