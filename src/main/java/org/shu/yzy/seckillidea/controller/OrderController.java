package org.shu.yzy.seckillidea.controller;

import lombok.extern.slf4j.Slf4j;
import org.shu.yzy.seckillidea.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class OrderController {
    @Autowired
    OrderService orderService;



}
