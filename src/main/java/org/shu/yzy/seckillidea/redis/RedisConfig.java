package org.shu.yzy.seckillidea.redis;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Component
@ConfigurationProperties(prefix = "redis")
@Data
public class RedisConfig {
    private String host;

    private String port;

    @Bean
    public JedisPool getJedisPool(){
        JedisPool jedisPool = new JedisPool();
        return jedisPool;
    }
}
