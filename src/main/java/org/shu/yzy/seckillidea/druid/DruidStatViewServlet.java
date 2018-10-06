package org.shu.yzy.seckillidea.druid;

import com.alibaba.druid.support.http.StatViewServlet;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

@WebServlet(urlPatterns = "/druid/*",
            initParams = {@WebInitParam(name = "allow", value = ""), // IP白名单
                            @WebInitParam(name = "loginUsername", value="yzy"), // 用户名
                            @WebInitParam(name = "loginPassword", value="123"), // 密码
                            @WebInitParam(name="resetEnable", value="false")}) // 禁止HTML页面上的RESET ALL 功能
public class DruidStatViewServlet extends StatViewServlet {
    private static final long serialVersionUID = 1L;
}
