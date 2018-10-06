package org.shu.yzy.seckillidea.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.shu.yzy.seckillidea.domain.MiaoshaUser;
import org.shu.yzy.seckillidea.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MiaoshaUserDaoTest {
    @Autowired
    MiaoshaUserDao miaoshaUserDao;

    @Test
    public void testSelect(){
        Page page = new Page(0, 1000);
        List<MiaoshaUser> userList = miaoshaUserDao.getUserList(page);
        for(MiaoshaUser user: userList){
            System.out.println(user.getId() + ",1");
        }
    }

}
