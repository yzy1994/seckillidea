package org.shu.yzy.seckillidea.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.shu.yzy.seckillidea.dao.OrderInfoDao;
import org.shu.yzy.seckillidea.domain.OrderInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderInfoDaoTest {
    @Autowired
    OrderInfoDao orderInfoDao;

    long userId = Long.parseLong("18201789699");
    long seckillId = Long.parseLong("1");

    @Test
    public void testInsert(){
        try {
            orderInfoDao.inserOrderInfo(userId, seckillId, new Date());
        } catch (DuplicateKeyException e){
            System.out.println("重复秒杀");
        }
    }

    @Test
    public void testUpdate(){
        orderInfoDao.payOrder(userId, seckillId);
    }

    @Test
    public void testSelect() {
        List<OrderInfo> orderInfoList = orderInfoDao.getOrderInfoList();
        for(OrderInfo orderInfo: orderInfoList){
            System.out.println(orderInfo);
        }
    }

}
