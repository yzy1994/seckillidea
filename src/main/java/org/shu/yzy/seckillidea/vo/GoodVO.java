package org.shu.yzy.seckillidea.vo;

import lombok.Data;
import org.shu.yzy.seckillidea.domain.Good;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class GoodVO extends Good {
    public static int NULLFLAG = -9999;

    private Long seckillId;

    /** 秒杀价格 */
    private BigDecimal seckillPrice;

    /** 秒杀库存 */
    private Integer seckillStock;

    /** 开始时间 */
    private Date startDate;

    /** 结束时间 */
    private Date endDate;

    public GoodVO(){};

    public static GoodVO getNullObject(){
        GoodVO nullObject = new GoodVO();
        nullObject.setSeckillStock(NULLFLAG);
        return nullObject;
    }

    public boolean isNull(){
        return seckillStock==NULLFLAG;
    }

    public String fullString(){
        return super.toString() + this.toString();
    }
}

