package org.shu.yzy.seckillidea.service.impl;

import org.shu.yzy.seckillidea.dao.GoodDao;
import org.shu.yzy.seckillidea.domain.SeckillGood;
import org.shu.yzy.seckillidea.service.GoodService;
import org.shu.yzy.seckillidea.vo.GoodVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodServiceImpl implements GoodService {

    @Autowired
    GoodDao goodDao;

    @Override
    public List<GoodVO> listGoodVO() {
        return goodDao.getGoodVOList();
    }

    @Override
    public GoodVO getGoodVOById(long goodId) {
        return goodDao.getGoodVOByGoodId(goodId);
    }

    @Override
    public SeckillGood getSeckillGoodById(long goodId) {
        return goodDao.getSeckillGoodByGoodId(goodId);
    }
}
