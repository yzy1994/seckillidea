package org.shu.yzy.seckillidea.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.shu.yzy.seckillidea.Enum.ResultEnum;
import org.shu.yzy.seckillidea.domain.OrderInfo;
import org.shu.yzy.seckillidea.domain.Page;
import org.shu.yzy.seckillidea.service.OrderService;
import org.shu.yzy.seckillidea.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@Slf4j
@RequestMapping("/seckill")
public class OrderController {
    @Autowired
    OrderService orderService;

    @RequestMapping("/order/{seckillId}")
    @ResponseBody
    public Result<List<OrderInfo>> listOrderBySeckillId(@PathVariable("seckillId") long seckillId, Page page){
        PageHelper.startPage(page.getStart(), page.getCount());
        List<OrderInfo> orderInfoList = orderService.getSeckillOrderInfoList(seckillId);
        return Result.getResult(ResultEnum.SUCCESS, orderInfoList);
    }

}
