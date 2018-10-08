package org.shu.yzy.seckillidea.controller;

import lombok.extern.slf4j.Slf4j;
import org.shu.yzy.seckillidea.Enum.ResultEnum;
import org.shu.yzy.seckillidea.access.AccessLimit;
import org.shu.yzy.seckillidea.domain.MiaoshaUser;
import org.shu.yzy.seckillidea.redis.GoodKey;
import org.shu.yzy.seckillidea.redis.RedisService;
import org.shu.yzy.seckillidea.service.GoodService;
import org.shu.yzy.seckillidea.vo.GoodVO;
import org.shu.yzy.seckillidea.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.shu.yzy.seckillidea.vo.*;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("good")
@Slf4j
public class GoodController extends BaseController {

    @Autowired
    GoodService goodService;

    @Autowired
    RedisService redisService;

    @AccessLimit(maxCount = 5, seconds = 10)
    @RequestMapping(value = "/list")
    public String list(MiaoshaUser user, HttpServletRequest request, HttpServletResponse response, Model model) {
        model.addAttribute("user", user);
        String cachedHtml = redisService.get(GoodKey.getGoodList, "", String.class);;
        if(StringUtils.isEmpty(cachedHtml)) {
            log.info("列表页没缓存, 需要查询数据库");
            List<GoodVO> goodVOList = goodService.listGoodVO();
            model.addAttribute("goodVOList", goodVOList);
        }
        return render(request, response, model, "goods_list", GoodKey.getGoodList, "", cachedHtml);
    }

    @AccessLimit(maxCount = 5, seconds = 10)
    @RequestMapping("/detail/{goodId}")
    @ResponseBody
    public Result<GoodDetailVO> detail(@PathVariable("goodId") long goodId, MiaoshaUser user) {
        log.info("访问了详情页" + goodId);
        ModelAndView mv = new ModelAndView();
        mv.addObject("user", user);

        GoodVO goodVO = redisService.get(GoodKey.getGoodKey, String.valueOf(goodId), GoodVO.class);
        //缓存查不到, 查询数据库
        if (goodVO == null) {
            goodVO = goodService.getGoodVOById(goodId);
        }
        //数据库也为空， (缓存穿透)缓存插入一个标识null的对象
        if (goodVO == null) {
            redisService.set(GoodKey.getGoodKey, String.valueOf(goodId), GoodVO.getNullObject());
        }

        //goodVO是一个空对象
        if (goodVO!=null && goodVO.isNull()) {
            goodVO = null;
        }

        if (goodVO == null) {
            return Result.getResult(ResultEnum.SECKILL_NOT_EXIST);
        }

        long startTime = goodVO.getStartDate().getTime();
        long endTime = goodVO.getEndDate().getTime();
        long now = System.currentTimeMillis();
        int stock = goodVO.getSeckillStock();

        int seckillStatus = 0;
        int remainSeconds = 0;

        if (now < startTime) {
            //秒杀活动未开启
            seckillStatus = 0;
            remainSeconds = (int) ((startTime - now) / 1000);
        } else if (now > endTime || stock<=0) {
            //秒杀活动已经结束
            seckillStatus = 2;
            remainSeconds = -1;
        } else {
            //秒杀进行中
            seckillStatus = 1;
            remainSeconds = 0;
        }

        GoodDetailVO goodDetailVO = new GoodDetailVO(seckillStatus, remainSeconds,
                goodVO, user);
        return Result.getResult(ResultEnum.SUCCESS, goodDetailVO);
    }
}
