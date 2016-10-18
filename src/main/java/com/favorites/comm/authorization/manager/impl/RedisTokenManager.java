package com.favorites.comm.authorization.manager.impl;

import com.favorites.comm.Const;
import com.favorites.comm.authorization.manager.TokenManager;
import com.favorites.comm.authorization.model.TokenModel;
import com.favorites.comm.utils.DateUtils;
import com.favorites.comm.utils.MD5Util;
import com.favorites.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 通过Redis存储和验证token的实现类
 */
@Service("tokenManager")
public class RedisTokenManager implements TokenManager {


    private RedisTemplate<Long, String> redis;

    @Autowired
    @Qualifier("redisTemplate")
    public void setRedis(RedisTemplate redis) {
        this.redis = redis;
        //泛型设置成Long后必须更改对应的序列化方案
        redis.setKeySerializer(new JdkSerializationRedisSerializer());
    }

    public TokenModel createToken(User user) {
        //使用uuid作为源token
        //String token = UUID.randomUUID().toString().replace("-", "");
        String token = user.getUserName() + user.getPassWord() + DateUtils.getCurrentTime() + Const.TOKEN_KEY;
        token = MD5Util.encrypt(token);
        TokenModel model = new TokenModel(user.getId(), token);
        //存储到redis并设置过期时间
        redis.boundValueOps(user.getId()).set(token, Const.TOKEN_EXPIRES_HOUR, TimeUnit.HOURS);
        return model;
    }

    public TokenModel getToken(String authentication) {
        if (authentication == null || authentication.length() == 0) {
            return null;
        }
        String[] param = authentication.split("_");
        if (param.length != 2) {
            return null;
        }
        //使用userId和源token简单拼接成的token，可以增加加密措施
        long userId = Long.parseLong(param[0]);
        String token = param[1];
        return new TokenModel(userId, token);
    }

    public boolean checkToken(TokenModel model) {
        if (model == null) {
            return false;
        }
        String token = redis.boundValueOps(model.getUserId()).get();
        if (token == null || !token.equals(model.getToken())) {
            return false;
        }
        //如果验证成功，说明此用户进行了一次有效操作，延长token的过期时间
        redis.boundValueOps(model.getUserId()).expire(Const.TOKEN_EXPIRES_HOUR, TimeUnit.HOURS);
        return true;
    }

    public void deleteToken(long userId) {
        redis.delete(userId);
    }
}
