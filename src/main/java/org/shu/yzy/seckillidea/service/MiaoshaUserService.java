package org.shu.yzy.seckillidea.service;

import org.shu.yzy.seckillidea.Enum.ResultEnum;
import org.shu.yzy.seckillidea.domain.MiaoshaUser;
import org.shu.yzy.seckillidea.exception.GlobalException;
import org.shu.yzy.seckillidea.vo.LoginVO;
import org.shu.yzy.seckillidea.vo.RegisterVO;

import javax.servlet.http.HttpServletResponse;

public interface MiaoshaUserService {

    MiaoshaUser getById(long id);

    ResultEnum login(HttpServletResponse response, LoginVO loginVO);

    ResultEnum register(RegisterVO registerVO) throws GlobalException;

    /** 根据cookie中的 token查询用户Session */
    MiaoshaUser getByToken(HttpServletResponse response, String token);


}
