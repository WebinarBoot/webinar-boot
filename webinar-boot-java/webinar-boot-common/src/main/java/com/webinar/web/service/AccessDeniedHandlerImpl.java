package com.webinar.web.service;

import com.alibaba.fastjson.JSON;
import com.webinar.utils.AuthExceptionUtil;
import com.webinar.utils.WebUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//自定义授权失败异常处理类
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest,
                       HttpServletResponse httpServletResponse,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        System.out.println("AccessDeniedHandler:暂无权限");
        WebUtils.rednerString(httpServletResponse, JSON.toJSONString(AuthExceptionUtil.getErrMsgByExceptionType(accessDeniedException)));

    }
}
