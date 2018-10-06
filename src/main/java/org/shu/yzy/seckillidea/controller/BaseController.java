package org.shu.yzy.seckillidea.controller;

import lombok.extern.slf4j.Slf4j;
import org.shu.yzy.seckillidea.redis.KeyPrefix;
import org.shu.yzy.seckillidea.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

@Controller
@Slf4j
public class BaseController {

    private boolean pageCacheEnable;

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

    @Autowired
    RedisService redisService;

    public String render(HttpServletRequest request, HttpServletResponse response, Model model, String tplName,
                         KeyPrefix prefix, String key, String cachedHtml) {
        //有页面缓存就直接输出
        if (!StringUtils.isEmpty(cachedHtml)) {
            out(response, cachedHtml);
            return null;
        }
        //手动渲染
        WebContext webContext = new WebContext(request, response, request.getServletContext(),
                request.getLocale(), model.asMap());
        String html = thymeleafViewResolver.getTemplateEngine().process(tplName, webContext);
        if (!StringUtils.isEmpty(html)) {
            redisService.set(prefix, key, html);
        }
        out(response, html);
        return null;
    }

    public static void out(HttpServletResponse response, String html) {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        try {
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(html.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
