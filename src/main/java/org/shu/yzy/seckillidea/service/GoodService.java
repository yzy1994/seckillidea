package org.shu.yzy.seckillidea.service;

import org.shu.yzy.seckillidea.domain.SeckillGood;
import org.shu.yzy.seckillidea.vo.GoodVO;

import java.util.List;

public interface GoodService {

    List<GoodVO> listGoodVO();

    GoodVO getGoodVOById(long goodId);

    /** 根据秒杀项Id查秒杀项详情 */
    SeckillGood getSeckillGoodById(long goodId);
}
