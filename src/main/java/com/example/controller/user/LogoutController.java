package com.example.controller.user;

import com.example.util.Constants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class LogoutController {

    @GetMapping("/logout.do")
    public String Logout(HttpServletRequest req, HttpSession session) {
        //移除用户的Constants.USER_SESSION
        req.getSession().removeAttribute(Constants.USER_SESSION);
        //使session失效
        session.invalidate();
        //返回登录页面
        return "redirect:/login.html";
        //return "login";
    }
}
