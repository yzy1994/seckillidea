package org.shu.yzy.seckillidea.domain;

import lombok.Data;
import org.shu.yzy.seckillidea.vo.GoodVO;

import java.math.BigDecimal;

@Data
public class Good {

    private Long goodId;

    private String goodName;

    private String goodTitle;

    private String goodImg;

    private String goodDetail;

    private BigDecimal goodPrice;

    private Integer goodStock;

    public Good(){};
}
