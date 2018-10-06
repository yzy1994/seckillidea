package org.shu.yzy.seckillidea.redis.impl;

import com.alibaba.fastjson.JSON;
import org.shu.yzy.seckillidea.redis.KeyPrefix;
import org.shu.yzy.seckillidea.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class RedisServiceImpl implements RedisService {
    @Autowired
    JedisPool jedisPool;

    @Override
    public <T> T get(KeyPrefix keyPrefix, String key, Class<T> clazz) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();

            String realKey = keyPrefix.getPrefix() + key;

            String str = jedis.get(realKey);
            T t = string2Bean(str, clazz);
            return t;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }

    }

    @Override
    public <T> T set(KeyPrefix keyPrefix, String key, T value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String str = bean2String(value);

            String realKey = keyPrefix.getPrefix() + key;
            int expireSeconds = keyPrefix.expireSeconds();

            if(expireSeconds<=0) {
                jedis.set(realKey, str);
            }else{
                jedis.setex(realKey, expireSeconds, str);
            }
            return value;
        }finally {
            if(jedis!=null) {
                jedis.close();
            }
        }
    }

    @Override
    public boolean exist(KeyPrefix keyPrefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = keyPrefix.getPrefix() + key;
            return jedis.exists(realKey);
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    @Override
    public Long incr(KeyPrefix keyPrefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = keyPrefix.getPrefix() + key;
            return jedis.incr(realKey);
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    @Override
    public Long decr(KeyPrefix keyPrefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = keyPrefix.getPrefix() + key;
            return jedis.decr(realKey);
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    private <T> T string2Bean(String str, Class<T> clazz) {
        if(str==null||str.length()<=0){
            return null;
        }

        if(clazz==int.class||clazz==Integer.class){
            return (T)Integer.valueOf(str);
        }else if(clazz==long.class||clazz==Long.class){
            return (T)Long.valueOf(str);
        }else if(clazz==String.class){
            return (T)str;
        }else{
            return JSON.toJavaObject(JSON.parseObject(str), clazz);
        }
    }

    private <T> String bean2String(T value) {
        if (value == null)
            return null;

        Class<?> clazz = value.getClass();
        if (clazz == int.class || clazz == Integer.class || clazz == long.class || clazz == Long.class) {
            return "" + value;
        } else if (clazz == String.class) {
            return (String)value;
        } else {
            return JSON.toJSONString(value);
        }
    }
}
