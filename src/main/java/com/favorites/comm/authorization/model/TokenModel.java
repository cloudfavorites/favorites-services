package com.favorites.comm.authorization.model;

/**
 * Token的Model类
 */
public class TokenModel {

    //用户id
    private long userId;

    //token
    private String token;

    public TokenModel(long userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
