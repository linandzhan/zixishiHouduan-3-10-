package com.zixishi.zhanwei.config.authorization.token;


import com.zixishi.zhanwei.mapper.TokenMapper;
import com.zixishi.zhanwei.model.Account;
import com.zixishi.zhanwei.model.Token;
import com.zixishi.zhanwei.util.Constants;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 通过Redis存储和验证token的实现类
 * @see
 * @author zwl
 */
@Component
public class RedisTokenManager implements TokenManager {
    @Resource
    private TokenMapper tokenMapper;

    @Resource
    private RedisTemplate<String, String> stringRedisTemplate;

//    @Resource
//    public void setRedis(RedisTemplate redis) {
//        this.redis = redis;
//        //泛型设置成Long后必须更改对应的序列化方案
//        redis.setKeySerializer(new JdkSerializationRedisSerializer());
//    }

    public TokenModel createToken(long userId) {
        //使用uuid作为源token
        String token = UUID.randomUUID().toString().replace("-", "");
        TokenModel model = new TokenModel(userId, token);
        //存储到redis并设置过期时间
        stringRedisTemplate.boundValueOps(String.valueOf(userId)).set(token, Constants.TOKEN_EXPIRES_HOUR, TimeUnit.HOURS);


        //存入Token中
        Token t = new Token();
        if(tokenMapper.countByAccount(userId) == 0) {
            t.setToken(token);
            t.setCreateTime(LocalDateTime.now());
            Account account = new Account();
            account.setId(userId);
            t.setAccount(account);
            Long save = tokenMapper.save(t);
            System.out.println(save);
        }else {
            Long tokenId = tokenMapper.findIdByAccount(userId);
            t.setToken(token);
            t.setId(tokenId);
            t.setCreateTime(LocalDateTime.now());
            tokenMapper.update(t);
        }

        return model;
    }

    public TokenModel getToken(String authentication) {
        if (authentication == null || authentication.length() == 0) {
            return null;
        }
//        String[] param = authentication.split("_");
//        if (param.length != 2) {
//            return null;
//        }
        //使用userId和源token简单拼接成的token，可以增加加密措施
        String token = authentication;
        long userId = tokenMapper.findAccountByToken(token);

        return new TokenModel(userId, token);
    }

    public boolean checkToken(TokenModel model) {
        if (model == null) {
            return false;
        }
        String token = (String) stringRedisTemplate.boundValueOps(String.valueOf(model.getId())).get();
        if (token == null || !token.equals(model.getToken())) {
            return false;
        }
        //如果验证成功，说明此用户进行了一次有效操作，延长token的过期时间
        stringRedisTemplate.boundValueOps(String.valueOf(model.getId())).expire(Constants.TOKEN_EXPIRES_HOUR, TimeUnit.HOURS);
        return true;
    }

    public void deleteToken(long userId) {
        stringRedisTemplate.delete(String.valueOf(userId));
        //按道理不应该根据account_id删除token,因为小程序和app可以同时登录的。这样已删除就一起删除掉了
        tokenMapper.deleteToken(userId);
    }
}
