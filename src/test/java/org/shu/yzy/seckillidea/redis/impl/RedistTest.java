package org.shu.yzy.seckillidea.redis.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.shu.yzy.seckillidea.redis.GoodKey;
import org.shu.yzy.seckillidea.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedistTest {

    @Autowired
    RedisService redisService;

    @Test
    public void redisTest(){
        System.out.println(redisService.exist(GoodKey.getSeckillStock, "avasdq"));
    }
}
