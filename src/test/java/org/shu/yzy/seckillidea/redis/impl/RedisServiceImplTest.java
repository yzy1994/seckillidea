package org.shu.yzy.seckillidea.redis.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.shu.yzy.seckillidea.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisServiceImplTest {
    @Autowired
    RedisService redisService;

    private static final String key = "yzy";

    private static final Long value = new Long(1);

    @Test
    public void name() {

    }

    @Test
    public void redisGet() {

    }
}