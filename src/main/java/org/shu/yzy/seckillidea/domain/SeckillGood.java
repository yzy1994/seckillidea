package org.shu.yzy.seckillidea.domain;

import lombok.Data;
import org.shu.yzy.seckillidea.vo.GoodVO;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class  SeckillGood {
    public static int NULLFLAG = -9999;

    /** 秒杀项id 主键 */
    private Long seckillid;

    /** 商品id */
    private Long goodId;

    /** 秒杀价格 */
    private BigDecimal sckillPrice;

    /** 秒杀库存 */
    private Integer seckillStock;

    /** 开始时间 */
    private Date startDate;

    /** 结束时间 */
    private Date endDate;

    public static SeckillGood getNullObject(){
        SeckillGood seckillGood = new SeckillGood();
        seckillGood.setSeckillStock(NULLFLAG);
        return seckillGood;
    }

    public boolean isNull(){
        return this.seckillStock == NULLFLAG;
    }

    public SeckillGood(){}

    public SeckillGood(GoodVO goodVO) {
        this.seckillid = goodVO.getSeckillId();
        this.goodId = goodVO.getGoodId();
        this.sckillPrice = goodVO.getSeckillPrice();
        this.seckillStock = goodVO.getSeckillStock();
        this.startDate = goodVO.getStartDate();
        this.endDate = goodVO.getEndDate();
    }
}
