package com.zixishi.zhanwei.config.authorization.token;

/**
 * Token的Model类，可以增加字段提高安全性，例如时间戳、url签名
 * @author zwl
 */
public class TokenModel {

    //用户id
    private long id;

    //随机生成的uuid
    private String token;

    public TokenModel(long id, String token) {
        this.id = id;
        this.token = token;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
