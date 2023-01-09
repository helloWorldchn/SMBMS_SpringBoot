package com.example.config;

import com.example.util.Constants;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginHandlerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //登录成功后，应该有用户的Session
        Object loginUser = request.getSession().getAttribute(Constants.USER_SESSION);

        if (loginUser == null) {//没有登录
            //request.setAttribute("error", "没有权限，请先登录");
            //request.getRequestDispatcher("/login.html").forward(request, response);
            request.getRequestDispatcher("/errorlogin.html").forward(request, response);
            return false;
        } else {
            return true;
        }
    }
}
