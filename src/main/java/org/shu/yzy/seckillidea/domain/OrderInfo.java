package org.shu.yzy.seckillidea.domain;

import lombok.Data;

import java.util.Date;

@Data
public class OrderInfo {
    private Long userId;

    private Long seckillId;

    private Date seckillDate;

    private Integer status;

    public OrderInfo(){}

    public OrderInfo(Long userId, Long seckillId) {
        this.userId = userId;
        this.seckillId = seckillId;
    }
}
