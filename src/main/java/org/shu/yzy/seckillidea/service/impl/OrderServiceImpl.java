package org.shu.yzy.seckillidea.service.impl;

import org.shu.yzy.seckillidea.dao.OrderInfoDao;
import org.shu.yzy.seckillidea.domain.OrderInfo;
import org.shu.yzy.seckillidea.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderInfoDao orderInfoDao;

    @Override
    public List<OrderInfo> getUserOrderInfoList(long userId) {
        return orderInfoDao.getUserOrderInfoList(userId);
    }

    @Override
    public List<OrderInfo> getSeckillOrderInfoList(long seckillId) {
        return orderInfoDao.getSeckillOrderInfoList(seckillId);
    }

    @Override
    public int payOrder(long userId, long seckillId) {
        return orderInfoDao.payOrder(userId, seckillId);
    }
}
