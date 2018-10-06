package org.shu.yzy.seckillidea.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SeckillAction implements Serializable {
    private long userId;

    private long seckillId;

    private long seckillDate;

    public SeckillAction(){}

    public SeckillAction(long userId, long seckillId, long seckillDate) {
        this.userId = userId;
        this.seckillId = seckillId;
        this.seckillDate = seckillDate;
    }
}
