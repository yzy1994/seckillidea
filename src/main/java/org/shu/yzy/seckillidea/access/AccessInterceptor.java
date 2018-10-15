package org.shu.yzy.seckillidea.access;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.shu.yzy.seckillidea.Enum.ResultEnum;
import org.shu.yzy.seckillidea.domain.MiaoshaUser;
import org.shu.yzy.seckillidea.redis.AccessKey;
import org.shu.yzy.seckillidea.redis.RedisService;
import org.shu.yzy.seckillidea.service.MiaoshaUserService;
import org.shu.yzy.seckillidea.service.impl.MiaoshaUserServiceImpl;
import org.shu.yzy.seckillidea.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.thymeleaf.util.StringUtils;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Service
@Slf4j
public class AccessInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    MiaoshaUserService userService;

    @Autowired
    RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info(String.valueOf(handler.getClass()));
        if(handler instanceof HandlerMethod){
            MiaoshaUser user = getUser(request, response);
            UserContext.setUser(user);
            HandlerMethod handlerMethod = (HandlerMethod)handler;
            AccessLimit accessLimit = handlerMethod.getMethodAnnotation(AccessLimit.class);
            if(accessLimit == null){
                return true;
            }
            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();
            boolean needLogin = accessLimit.needLogin();
            if(needLogin&&user==null){
                //render(response, Result.getResult(ResultEnum.SESSION_ERROR));
                response.sendRedirect("/login/to_login");
                return false;
            }

            AccessKey accessKey = AccessKey.getAccessKey(seconds);
            String key = handlerMethod.getMethod().getName() + user.getId();
            log.info(key);
            // 获取最近访问次数
            Integer count = redisService.get(accessKey, key, Integer.class);
            if(count == null){
                redisService.set(accessKey, key, 1);
            } else if(count < maxCount){
                redisService.incr(accessKey, key);
            } else{
                render(response, Result.getResult(ResultEnum.ACCESS_LIMIT_REACHED));
            }
            return true;
        }
        return true;
    }

    /** response输出结果 */
    private void render(HttpServletResponse response, Result result) throws IOException {
        OutputStream outputStream = response.getOutputStream();
        String str = JSON.toJSONString(result);
        outputStream.write(str.getBytes("UTF-8"));
        outputStream.flush();
        outputStream.close();
    }

    private String getCookieValue(HttpServletRequest request, String cookieName){
        Cookie[] cookies = request.getCookies();
        if(cookies == null|| cookies.length==0){
            return null;
        }
        for(Cookie cookie: cookies){
            if(cookie.getName().equals(cookieName)){
                return cookie.getValue();
            }
        }
        return null;
    }

    private MiaoshaUser getUser(HttpServletRequest request, HttpServletResponse response){
        String paramToken = request.getParameter(MiaoshaUserServiceImpl.COOKIE_NAME_TOKEN);
        String cookieToken = getCookieValue(request, MiaoshaUserServiceImpl.COOKIE_NAME_TOKEN);
        if(StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)){
            return null;
        }
        String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
        return userService.getByToken(response, token);
    }
}
