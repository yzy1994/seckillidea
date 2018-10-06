package org.shu.yzy.seckillidea.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.shu.yzy.seckillidea.domain.OrderInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GoodServiceTest {

    @Autowired
    GoodService goodService;

    @Test
    public void test(){
        Long userId = Long.parseLong("18201789699");
        Long seckillGoodId = new Long(1);
        OrderInfo orderInfo = new OrderInfo(userId, seckillGoodId);
    }
}
