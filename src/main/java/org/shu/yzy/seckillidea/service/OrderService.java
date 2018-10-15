package org.shu.yzy.seckillidea.service;

import org.shu.yzy.seckillidea.domain.OrderInfo;
import org.shu.yzy.seckillidea.domain.Page;

import java.util.List;

public interface OrderService {

    /** 根据用户名获取订单列表 */
    List<OrderInfo> getUserOrderInfoList(long userId);

    /** 根据秒杀项Id 获取订单列表 */
    List<OrderInfo> getSeckillOrderInfoList(long seckillId);

    /** 支付订单(修改订单状态) */
    int payOrder(long userId, long seckillId);
}
