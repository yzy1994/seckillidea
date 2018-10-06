package org.shu.yzy.seckillidea.activemq;

import lombok.extern.slf4j.Slf4j;
import org.shu.yzy.seckillidea.dto.SeckillAction;
import org.shu.yzy.seckillidea.exception.GlobalException;
import org.shu.yzy.seckillidea.redis.GoodKey;
import org.shu.yzy.seckillidea.redis.RedisService;
import org.shu.yzy.seckillidea.service.SeckillService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;

@Component
@Slf4j
public class Consumer {
    @Autowired
    SeckillService seckillService;

    @Autowired
    RedisService redisService;

    @JmsListener(destination = MQConfig.DESTINATION)
    public void receiveQueue(ObjectMessage objectMessage) throws JMSException {
        SeckillAction seckillAction = new SeckillAction();
        BeanUtils.copyProperties(objectMessage.getObject(), seckillAction);
        log.info("消费了一个秒杀对象" + seckillAction.toString());
        try{
            seckillService.executeSeckill(seckillAction);
        }catch (GlobalException e){
            log.info(e.getMessage());
            //秒杀失败 redis库存 +1
            redisService.incr(GoodKey.getSeckillStock, String.valueOf(seckillAction.getSeckillId()));
        }
    }

}
