package org.shu.yzy.seckillidea;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.util.StringUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SeckillideaApplicationTests {

    @Test
    public void contextLoads() {
        String salt = StringUtils.randomAlphanumeric(10);
        System.out.println(salt);
    }

}
