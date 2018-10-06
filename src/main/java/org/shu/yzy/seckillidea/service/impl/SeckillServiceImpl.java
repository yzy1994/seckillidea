package org.shu.yzy.seckillidea.service.impl;

import org.shu.yzy.seckillidea.Enum.ResultEnum;
import org.shu.yzy.seckillidea.dao.GoodDao;
import org.shu.yzy.seckillidea.dao.OrderInfoDao;
import org.shu.yzy.seckillidea.dto.SeckillAction;
import org.shu.yzy.seckillidea.exception.GlobalException;
import org.shu.yzy.seckillidea.redis.RedisService;
import org.shu.yzy.seckillidea.redis.SeckillKey;
import org.shu.yzy.seckillidea.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.Queue;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class SeckillServiceImpl implements SeckillService {
    @Autowired
    OrderInfoDao orderInfoDao;

    @Autowired
    GoodDao goodDao;

    @Autowired
    JmsMessagingTemplate jmsMessagingTemplate;

    @Autowired
    Queue seckillQueue;

    @Autowired
    RedisService redisService;

    @Override
    @Transactional
    public void executeSeckill(SeckillAction seckillOrder) throws GlobalException{
        Long seckillId = seckillOrder.getSeckillId();
        Long userId = seckillOrder.getUserId();
        Date date = new Date(seckillOrder.getSeckillDate());

        //插入一条
        try {
            orderInfoDao.inserOrderInfo(userId, seckillId, date);
        }catch (DuplicateKeyException e){
            //重复秒杀
            throw new GlobalException(ResultEnum.REPEATED_SECKILL);
        }

        //减库存
        int reduceResult = goodDao.reduceStock(seckillId);
        if (reduceResult != 1) {
            // 减库存失败
            throw new GlobalException(ResultEnum.SECKILL_FAILED);
        }

        //秒杀操作执行成功, 向redis插入一条记录
        redisService.set(SeckillKey.getSeckillResult, userId + "," + seckillId, "success");
    }

    @Override
    public void productSeckillAction(SeckillAction seckillAction) {
        Map<String, Object> header = new HashMap<>();
        header.put("type", 1);
        jmsMessagingTemplate.convertAndSend(this.seckillQueue, seckillAction, header);
    }
}
