package org.shu.yzy.seckillidea.service;

import org.shu.yzy.seckillidea.dto.SeckillAction;
import org.shu.yzy.seckillidea.exception.GlobalException;

public interface SeckillService {

    /** 执行秒杀操作 */
    void executeSeckill(SeckillAction seckillAction) throws GlobalException;

    /** 将秒杀操作加入消息队列 */
    void productSeckillAction(SeckillAction seckillAction);
}
