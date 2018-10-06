package org.shu.yzy.seckillidea.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.shu.yzy.seckillidea.domain.Good;
import org.shu.yzy.seckillidea.domain.SeckillGood;
import org.shu.yzy.seckillidea.vo.GoodVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GoodDaoTest {

    @Autowired
    GoodDao goodDao;

    @Test
    public void testGoodVOList(){
        List<GoodVO> resList = goodDao.getGoodVOList();
        for(GoodVO goodVO: resList){
            System.out.println(goodVO.fullString());
        }
    }

    @Test
    public void testGoodVOByGoodId(){
        GoodVO goodVO = goodDao.getGoodVOByGoodId(1);
        System.out.println(goodVO.fullString());
    }

    @Test
    public void testReduceStock(){
        int stock = goodDao.reduceStock(1);
        System.out.println(stock);
    }

    @Test
    public void testSelect(){
        SeckillGood seckillGood = goodDao.getSeckillGoodByGoodId(new Long(1));
        System.out.println(seckillGood);
    }

}