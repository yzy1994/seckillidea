package org.shu.yzy.seckillidea.controller;

import com.mysql.jdbc.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.shu.yzy.seckillidea.Enum.ResultEnum;
import org.shu.yzy.seckillidea.domain.MiaoshaUser;
import org.shu.yzy.seckillidea.exception.GlobalException;
import org.shu.yzy.seckillidea.service.MiaoshaUserService;
import org.shu.yzy.seckillidea.utils.ValidatorUtil;
import org.shu.yzy.seckillidea.vo.LoginVO;
import org.shu.yzy.seckillidea.vo.RegisterVO;
import org.shu.yzy.seckillidea.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping("/login")
@Slf4j
public class LoginController {

    @Autowired
    MiaoshaUserService miaoshaUserService;

    @RequestMapping("/to_login")
    public String toLogin(MiaoshaUser user) {
        if (user != null) {
            return "redirect:/good/list";
        }
        return "login";
    }

    @RequestMapping("/do_login")
    @ResponseBody
    public Result<String> doLogin(@Valid LoginVO loginVO, HttpServletResponse response, MiaoshaUser user) {
        //TODO
        if (user == null) {
            log.info(loginVO.toString());
            String passInput = loginVO.getPassword();
            String mobile = loginVO.getMobile();
            if (StringUtils.isEmptyOrWhitespaceOnly(passInput)) {
                return Result.getResult(ResultEnum.PASSWORD_EMPTY);
            }
            if (StringUtils.isEmptyOrWhitespaceOnly(mobile)) {
                return Result.getResult(ResultEnum.MOBILE_EMPTY);
            }
            if (!ValidatorUtil.isMobile(mobile)) {
                return Result.getResult(ResultEnum.MOBILE_ERROR);
            }
        }

        ResultEnum resultEnum = miaoshaUserService.login(response, loginVO);

        return Result.getResult(resultEnum);
    }

    @RequestMapping("/to_register")
    public String toRegister() {
        return "register";
    }

    @ResponseBody
    @RequestMapping("/register")
    public Result register(@Valid RegisterVO registerVO) {
        ResultEnum resultEnum;
        try {
            resultEnum = miaoshaUserService.register(registerVO);
        } catch (GlobalException e) {
            return Result.getResult(e.getCode(), e.getMessage());
        }
        return Result.getResult(resultEnum);
    }
}
