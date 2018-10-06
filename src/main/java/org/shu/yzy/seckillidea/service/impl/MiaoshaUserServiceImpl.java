package org.shu.yzy.seckillidea.service.impl;

import org.shu.yzy.seckillidea.Enum.ResultEnum;
import org.shu.yzy.seckillidea.dao.MiaoshaUserDao;
import org.shu.yzy.seckillidea.domain.MiaoshaUser;
import org.shu.yzy.seckillidea.exception.GlobalException;
import org.shu.yzy.seckillidea.redis.MiaoshaUserKey;
import org.shu.yzy.seckillidea.redis.RedisService;
import org.shu.yzy.seckillidea.service.MiaoshaUserService;
import org.shu.yzy.seckillidea.utils.MD5Util;
import org.shu.yzy.seckillidea.utils.UUIDUtil;
import org.shu.yzy.seckillidea.vo.LoginVO;
import org.shu.yzy.seckillidea.vo.RegisterVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class MiaoshaUserServiceImpl implements MiaoshaUserService {

    public static final String COOKIE_NAME_TOKEN = "token";

    @Autowired
    MiaoshaUserDao miaoshaUserDao;

    @Autowired
    RedisService redisService;

    @Override
    public MiaoshaUser getById(long id) {
        return miaoshaUserDao.getById(id);
    }

    @Override
    public ResultEnum login(HttpServletResponse response,LoginVO loginVO) throws GlobalException{
        if(loginVO == null){
            return ResultEnum.SERVER_ERROR;
        }
        String mobile = loginVO.getMobile();
        String formPassword = loginVO.getPassword();

        MiaoshaUser miaoshaUser = getById(Long.parseLong(mobile));
        if(miaoshaUser==null){
            return ResultEnum.MOBILE_NOT_EXIST;
        }

        //密码验证
        String dbPassword = miaoshaUser.getPassword();
        String salt = miaoshaUser.getSalt();
        String calcPassword = MD5Util.getMD5(formPassword, salt);
        if(!calcPassword.equals(dbPassword)){
            return ResultEnum.PASSWORD_ERROR;
        }

        //生成cookie
        String token = UUIDUtil.uuid();
        addCookie(response, token, miaoshaUser);
        return ResultEnum.SUCCESS;
    }

    private void addCookie(HttpServletResponse response, String token, MiaoshaUser user){
        redisService.set(MiaoshaUserKey.token, token, user);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }


    @Override
    public ResultEnum register(RegisterVO registerVO) throws GlobalException {

        MiaoshaUser miaoshaUser = miaoshaUserDao.getById(Long.parseLong(registerVO.getMobile()));

        if(miaoshaUser!=null){
            throw new GlobalException(ResultEnum.MOBILE_EXISTED);
        }

        String salt = StringUtils.randomAlphanumeric(6);
        String dbPassword = MD5Util.getMD5(registerVO.getPassword(), salt);
        miaoshaUserDao.insertUser(Long.parseLong(registerVO.getMobile()), registerVO.getNickname(), dbPassword, salt);

        return ResultEnum.SUCCESS;
    }

    @Override
    public MiaoshaUser getByToken(HttpServletResponse response, String token) {
        if(StringUtils.isEmpty(token)){
            return null;
        }

        MiaoshaUser user = redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);
        //如果存在,延长有效期
        if(user != null){
            addCookie(response, token, user);
        }

        return user;
    }
}
