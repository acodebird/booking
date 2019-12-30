package com.booking.component.interceptor;

import com.booking.domain.User;
import com.booking.service.LoginServiceImpl;
import com.booking.utils.ResponseEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;

public class TypeInterceptor implements HandlerInterceptor {
    @Autowired
    private ObjectMapper objectMapper;
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        HttpSession session=httpServletRequest.getSession();
        User user= (User) session.getAttribute(LoginServiceImpl.LOGIN_SESSION_TOKEN);
        if(null==user||1!=user.getType()){
            System.out.println("不是管理员");
            httpServletResponse.setHeader("content-type", "application/json;charset=UTF-8");
            httpServletResponse.sendError(403, "forbidden");
            PrintWriter out = httpServletResponse.getWriter();
            out.write(objectMapper.writeValueAsString(ResponseEntity.ofFailed().status(HttpStatus.BAD_REQUEST).data("forbidden")));
            out.close();
            return false;
        }
        System.out.println("是管理员");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
    }
}
