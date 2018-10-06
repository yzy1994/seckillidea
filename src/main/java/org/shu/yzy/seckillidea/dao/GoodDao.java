package org.shu.yzy.seckillidea.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.shu.yzy.seckillidea.domain.SeckillGood;
import org.shu.yzy.seckillidea.vo.GoodVO;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Repository
@Mapper
public interface GoodDao {

    @Select("select g.*, sg.* " +
            "from seckill_good as sg " +
            "left join good as g " +
            "on g.good_id = sg.good_id " +
            "where sg.effective = 1")
    List<GoodVO> getGoodVOList();

    @Select("select g.*, sg.* " +
            "from seckill_good as sg " +
            "left join good as g " +
            "on g.good_id = sg.good_id " +
            "where sg.seckill_id = #{seckillId}")
    GoodVO getGoodVOByGoodId(@Param("seckillId") long seckillId);

    @Select("select * from seckill_good where good_id = #{goodId}")
    SeckillGood getSeckillGoodByGoodId(@Param("goodId") long goodId);

    /** 返回修改的行数 1: 减库存成功 0: 减库存失败 */
    @Update("update seckill_good set seckill_stock = seckill_stock - 1 where seckill_id = #{seckillId} and seckill_stock > 0")
    int reduceStock(@Param("seckillId") long seckillId);

    /** 返回修改的行数 1: 修改成功 0: 修改失败 */
    @Update("update seckill_good set seckill_stock = #{seckillStock} where seckill_id = #{seckillId}")
    int resetStock(SeckillGood g);
}
