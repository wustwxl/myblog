package com.wust.interceptor;


import com.wust.constant.WebConst;
import com.wust.dto.Types;
import com.wust.entity.Vo.OptionVo;
import com.wust.entity.Vo.UserVo;
import com.wust.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义拦截器
 * Created by BlueT on 2017/3/9.
 */
@Component
public class BaseInterceptor implements HandlerInterceptor {
    private static final Logger LOGGE = LoggerFactory.getLogger(BaseInterceptor.class);
    private static final String USER_AGENT = "user-agent";

    private MapCache cache = MapCache.single();

    @Resource
    private Commons commons;

    @Resource
    private AdminCommons adminCommons;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        String contextPath = request.getContextPath();
        // System.out.println(contextPath);
        String uri = request.getRequestURI();

        LOGGE.info("UserAgent: {}", request.getHeader(USER_AGENT));
        LOGGE.info("用户访问地址: {}, 来路地址: {}", uri, IPKit.getIpAddrByRequest(request));


        UserVo user = new UserVo();
        user.setUsername("admin");
        user.setEmail("1034683568@qq.com");
        request.getSession().setAttribute(WebConst.LOGIN_SESSION_KEY, user);

/*

 */


        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        OptionVo ov = new OptionVo();
        ov.setName("site_record");
        ov.setValue("");
        ov.setDescription("备案号");
        httpServletRequest.setAttribute("commons", commons);//一些工具类和公共方法
        httpServletRequest.setAttribute("option", ov);
        httpServletRequest.setAttribute("adminCommons", adminCommons);
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
