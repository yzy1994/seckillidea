package org.shu.yzy.seckillidea.dao;

import org.apache.ibatis.annotations.*;
import org.shu.yzy.seckillidea.domain.OrderInfo;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Date;
import java.util.List;

@Repository
@Mapper
public interface OrderInfoDao {

    @Insert("insert into order_info(user_id, seckill_id, seckill_date) " +
            "values(#{user_id}, #{seckill_id}, #{date})")
    int inserOrderInfo(@Param("user_id") long userId,
                       @Param("seckill_id") long seckillId,
                       @Param(value = "date")Date date);

    @Update("update order_info set status=1 " +
            "where user_id=#{user_id} " +
            "and seckill_id=#{seckill_id}")
    int payOrder(@Param("user_id") long userId,
                 @Param("seckill_id") long seckillId);

    @Select("select user_id, seckill_id, seckill_date, status " +
            "from order_info ")
    List<OrderInfo> getOrderInfoList();

    @Select("select user_id, seckill_id, seckill_date, status " +
            "from order_info " +
            "where user_id=#{user_id} ")
    List<OrderInfo> getUserOrderInfoList(@Param("user_id") long userId);

    @Select("select user_id, seckill_id, seckill_date, status " +
            "from order_info " +
            "where seckill_id=#{seckill_id} ")
    List<OrderInfo> getSeckillOrderInfoList(@Param("seckill_id") long seckillId);
}
