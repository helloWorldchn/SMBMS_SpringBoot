package com.example.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//如果想diy一些定制画的功能，只要写这个组件，然后将它交给SpringBoot，SpringBoot就会帮我们自动装配！
//扩展SpringMVC
@Controller
public class MyMVCConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("login");
        registry.addViewController("/login.html").setViewName("login");
        registry.addViewController("/errorlogin.html").setViewName("errorlogin");
        registry.addViewController("/frame.html").setViewName("frame");
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginHandlerInterceptor())
                .addPathPatterns("/jsp/**", "/frame.html")
                .excludePathPatterns("/login.html", "/", "/login.do", "/css/**", "/js/**", "/img/**","/errorlogin.html");
    }

}
