package org.shu.yzy.seckillidea.vo;

import lombok.Data;
import org.shu.yzy.seckillidea.domain.MiaoshaUser;

@Data
public class GoodDetailVO {
    private int miaoshaStatus = 0;
    private int remainSeconds = 0;
    private GoodVO good ;
    private MiaoshaUser user;

    public GoodDetailVO(int miaoshaStatus, int remainSeconds, GoodVO good, MiaoshaUser user) {
        this.miaoshaStatus = miaoshaStatus;
        this.remainSeconds = remainSeconds;
        this.good = good;
        this.user = user;
    }
}
