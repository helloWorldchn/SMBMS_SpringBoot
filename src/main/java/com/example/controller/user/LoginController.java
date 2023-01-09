package com.example.controller.user;

import com.example.pojo.User;
import com.example.service.user.UserService;
import com.example.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @RequestMapping("/login.do")
    public String Login(String userCode, String userPassword, HttpSession session, Model model){
        System.out.println("LoginServlet--start...");

        //和数据库中的密码进行对比，调用业务层
        User user = userService.login(userCode, userPassword);

        if (user!=null && userPassword.equals(user.getUserPassword())){
            //将用户的信息放到Session中；
            model.addAttribute(Constants.USER_SESSION,user);
            session.setAttribute(Constants.USER_SESSION, user);
            //跳转到主页
            //return "frame";
            return "redirect:/frame.html";
        }else {//查无此人，无法登陆
            //转发回登录界面，顺带提示，用户名或密码错误
            model.addAttribute("error","用户名或者密码错误！");
            return "login";
        }
    }
    //@RequestMapping("/frame.html")
    //public String toFrame(){
    //    return "frame";
    //}
}
