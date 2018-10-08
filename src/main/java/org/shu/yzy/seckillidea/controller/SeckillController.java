package org.shu.yzy.seckillidea.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.shu.yzy.seckillidea.Enum.ResultEnum;
import org.shu.yzy.seckillidea.access.AccessLimit;
import org.shu.yzy.seckillidea.dao.MiaoshaUserDao;
import org.shu.yzy.seckillidea.domain.MiaoshaUser;
import org.shu.yzy.seckillidea.domain.Page;
import org.shu.yzy.seckillidea.domain.SeckillGood;
import org.shu.yzy.seckillidea.dto.SeckillAction;
import org.shu.yzy.seckillidea.exception.GlobalException;
import org.shu.yzy.seckillidea.redis.GoodKey;
import org.shu.yzy.seckillidea.redis.MiaoshaUserKey;
import org.shu.yzy.seckillidea.redis.RedisService;
import org.shu.yzy.seckillidea.redis.SeckillKey;
import org.shu.yzy.seckillidea.service.GoodService;
import org.shu.yzy.seckillidea.service.OtherService;
import org.shu.yzy.seckillidea.service.SeckillService;
import org.shu.yzy.seckillidea.service.impl.MiaoshaUserServiceImpl;
import org.shu.yzy.seckillidea.utils.MD5Util;
import org.shu.yzy.seckillidea.vo.GoodVO;
import org.shu.yzy.seckillidea.vo.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/seckill")
@Slf4j
public class SeckillController implements InitializingBean {

    @Autowired
    RedisService redisService;

    @Autowired
    GoodService goodService;

    @Autowired
    OtherService otherService;

    @Autowired
    SeckillService seckillService;

    @Autowired
    MiaoshaUserDao miaoshaUserDao;

    /**
     * 获取秒杀验证码
     * goodId 秒杀项的Id
     */
    @AccessLimit(maxCount = 2, seconds = 10)
    @RequestMapping("/path/{goodId}")
    @ResponseBody
    public Result<String> getSeckillPath(@PathVariable("goodId") long goodId, MiaoshaUser user) {
        GoodKey seckillGoodKey = GoodKey.getSeckillGoodKey;
        SeckillGood seckillGood = getSeckillGood(goodId, seckillGoodKey);
        if (seckillGood == null) {
            return Result.getResult(ResultEnum.SECKILL_NOT_EXIST);
        }

        Date now = new Date();

        if (now.getTime() < seckillGood.getStartDate().getTime())
            return Result.getResult(ResultEnum.SECKILL_NOT_STARTED);

        //TODO
        if (now.getTime() > seckillGood.getEndDate().getTime() || seckillGood.getSeckillStock() == 0)
            return Result.getResult(ResultEnum.SECKILL_OVER);

        //获取秒杀接口 /execute/MD5验证码
        String seckillVC = MD5Util.getMD5(String.valueOf(goodId), SeckillKey.VC_SALT);

        return Result.getResult(ResultEnum.SUCCESS, "/seckill/execute/" + seckillVC);
    }

    /**
     * 获取SeckillGood 对象 先查redis 查不到查数据库 缓存穿透则插入一条空对象
     */
    private SeckillGood getSeckillGood(long seckillGoodId, GoodKey seckillGoodKey) {

        SeckillGood seckillGood = redisService.get(seckillGoodKey, String.valueOf(seckillGoodId), SeckillGood.class);

        //redis查不到， 查数据库
        if (seckillGood == null) {
            seckillGood = goodService.getSeckillGoodById(seckillGoodId);
        }

        //插入一个标识空的对象
        if (seckillGood == null) {
            log.info("缓存穿透 seckillId = " + seckillGoodId);
            redisService.set(seckillGoodKey, String.valueOf(seckillGoodId), SeckillGood.getNullObject());
        }

        if (seckillGood != null && seckillGood.isNull()) {
            seckillGood = null;
        }

        return seckillGood;
    }

    /**
     * 执行秒杀操作接口, 将秒杀请求加入消息队列
     *
     * @param goodId 秒杀项Id
     * @param vc     秒杀接口验证码
     * @param picvc  图片验证码
     * @param user   当前用户
     * @return
     */
    @AccessLimit(maxCount = 1, seconds = 10)
    @RequestMapping("/execute/{vc}/{goodId}/{picvc}")
    @ResponseBody
    public Result<ResultEnum> executeSeckill(@PathVariable("goodId") long goodId,
                                             @PathVariable("vc") String vc,
                                             @PathVariable("picvc") int picvc,
                                             MiaoshaUser user) {

        SeckillGood seckillGood = getSeckillGood(goodId, GoodKey.getSeckillGoodKey);

        // 验证接口验证码
        if (seckillGood == null) {
            return Result.getResult(ResultEnum.SECKILL_NOT_EXIST);
        }

        String realVC = MD5Util.getMD5(String.valueOf(goodId), SeckillKey.VC_SALT);
        if (!realVC.equals(vc)) {
            return Result.getResult(ResultEnum.SECKILL_FALSE_VC);
        }

        //获取图形验证码的答案
        String answerString = redisService.get(SeckillKey.getSeckillVC, user.getId() + "," + goodId, String.class);
        if (answerString == null) {
            return Result.getResult(ResultEnum.SECKILL_VC_OVERDUED);
        }
        int answer = Integer.parseInt(answerString);

        if (answer != picvc) {
            return Result.getResult(ResultEnum.SECKILL_FALSE_VC);
        }

        return executeSeckill(goodId, user);
    }

    @RequestMapping("/execute/{goodId}")
    @ResponseBody
    public Result<ResultEnum> executeSeckill(@PathVariable("goodId") long goodId,
                                             MiaoshaUser user) {
        if (user == null) {
            log.info("用户未登录");
            return Result.getResult(ResultEnum.SESSION_ERROR);
        }

        SeckillGood seckillGood = getSeckillGood(goodId, GoodKey.getSeckillGoodKey);
        //redis 预减库存
        if (!redisService.exist(GoodKey.getSeckillStock, String.valueOf(goodId))) {
            redisService.set(GoodKey.getSeckillGoodKey, String.valueOf(goodId), seckillGood.getSeckillStock());
        }

        long stock = redisService.decr(GoodKey.getSeckillStock, String.valueOf(goodId));

        if (stock < 0) {
            return Result.getResult(ResultEnum.SECKILL_OVER);
        }

        //秒杀操作加入消息队列
        SeckillAction seckillAction = new SeckillAction(user.getId(), goodId, System.currentTimeMillis());
        log.info("将秒杀操作加入消息队列" + seckillAction);
        try {
            seckillService.productSeckillAction(seckillAction);
        } catch (GlobalException e) {
            return Result.getResult(e.getCode(), e.getMessage());
        }

        //排队中
        return Result.getResult(ResultEnum.SUCCESS);
    }

    /**
     * 获取图形验证码
     *
     * @param user
     * @param goodId   秒杀项Id
     * @param response
     * @return
     */
    @AccessLimit(maxCount = 2, seconds = 5)
    @RequestMapping(value = "/verifyCode/{goodId}/{timestamp}", method = RequestMethod.GET)
    @ResponseBody
    public Result<String> getSeckillVerifyCode(MiaoshaUser user, @PathVariable("goodId") long goodId,
                                               HttpServletResponse response,
                                               @PathVariable("timestamp") long timestamp) {
        SeckillGood seckillGood = getSeckillGood(goodId, GoodKey.getSeckillGoodKey);
        long nowTimestamp = System.currentTimeMillis();
        if (nowTimestamp < seckillGood.getStartDate().getTime()) {
            return Result.getResult(ResultEnum.SECKILL_NOT_STARTED);
        }

        if (nowTimestamp > seckillGood.getEndDate().getTime() || seckillGood.getSeckillStock() <= 0) {
            return Result.getResult(ResultEnum.SECKILL_OVER);
        }

        if (user == null) {
            return Result.getResult(ResultEnum.SESSION_ERROR);
        }

        try {
            BufferedImage image = otherService.createVerifyCode(user, goodId);
            OutputStream out = response.getOutputStream();
            ImageIO.write(image, "JPEG", out);
            out.flush();
            out.close();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return Result.getResult(ResultEnum.SECKILL_FALSE_VC);
        }
    }

    /**
     * 用户轮询获取 即时秒杀结果(redis保存10分钟) 接口
     */
    @RequestMapping(value = "/result/{seckillId}", method = RequestMethod.GET)
    @ResponseBody
    public Result<String> getSeckillResult(@PathVariable("seckillId") long seckillId,
                                           MiaoshaUser user) {
        if (user == null) {
            return Result.getResult(ResultEnum.SESSION_ERROR);
        }

        GoodKey seckillGoodKey = GoodKey.getSeckillGoodKey;
        SeckillGood seckillGood = getSeckillGood(seckillId, seckillGoodKey);

        if (redisService.exist(SeckillKey.getSeckillResult, user.getId() + "," + seckillId)) {
            return Result.getResult(ResultEnum.SECKILL_SUCCESS);
        } else {
            if (seckillGood.getSeckillStock() == 0) {
                // 库存为0 秒杀失败
                return Result.getResult(ResultEnum.SECKILL_FAILED);
            } else {
                // 继续下一次轮询
                return Result.getResult(ResultEnum.SECKILL_WAIT_RESULT);
            }
        }
    }

    /**
     * 系统初始化
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodVO> goodVOList = goodService.listGoodVO();
        if (goodVOList == null) {
            return;
        }

        for (GoodVO goodVO : goodVOList) {
            redisService.set(GoodKey.getSeckillStock, String.valueOf(goodVO.getGoodId()),
                    goodVO.getSeckillStock());

            SeckillGood seckillGood = new SeckillGood(goodVO);

            redisService.set(GoodKey.getSeckillGoodKey, String.valueOf(goodVO.getGoodId()),
                    seckillGood);
        }

        generateTestSession();
    }

    /**
     * 生成测试用Session 前1000个用户 token=userId
     */
    private void generateTestSession() {
        Page page = new Page(0, 1000);
        List<MiaoshaUser> userList = miaoshaUserDao.getUserList(page);
        for (MiaoshaUser user : userList) {
            System.out.println(user);
        }

        for (MiaoshaUser user : userList) {
            String token = String.valueOf(user.getId());
            redisService.set(MiaoshaUserKey.token, token, user);
        }
    }

}
