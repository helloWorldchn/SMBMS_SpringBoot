package com.example.controller.user;

import com.alibaba.fastjson.JSONArray;
import com.example.pojo.Role;
import com.example.pojo.User;
import com.example.service.role.RoleService;
import com.example.service.user.UserService;

import com.example.util.Constants;
import com.example.util.PageSupport;
import com.mysql.jdbc.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.util.Constants.USER_SESSION;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    //跳转到修改密码的页面pwdmodify.jsp
    @RequestMapping("/jsp/pwdmodify.do")
    public String toUpdatePwd(){
        return "pwdmodify";
    }

    //修改密码
    @RequestMapping(value = "/jsp/user.do", params = {"method=savepwd"})
    public String updatePwd(String newpassword, HttpServletRequest req, Model model){
        //从Session里面拿id
        User attribute = (User) req.getSession().getAttribute(USER_SESSION);

        boolean flag = false;
        //判断Session和密码是否为空
        if (attribute != null && !StringUtils.isNullOrEmpty(newpassword)){
            flag = userService.updatePwd(attribute.getId(),newpassword);
            if(flag){
                model.addAttribute("message","修改密码成功，请退出，用新密码登录");
                //密码修改成功，移除当前Session
                req.getSession().removeAttribute(USER_SESSION);
                return "errorlogin";
            }else {
                model.addAttribute("message","修改密码失败");
            }
        }else {
            model.addAttribute("message","新密码有问题");
        }
        //修改完毕后回到密码修改界面
        return "pwdmodify";
    }

    //验证旧密码，session中有用户的密码
    @RequestMapping(value = "/jsp/user.do", params = {"method=pwdmodify"})
    @ResponseBody
    public String pwdModify(String oldpassword, HttpServletRequest req) {
        //从Session里面拿ID；
        User attribute = (User) req.getSession().getAttribute(USER_SESSION);

        //万能的Map，结果集。将结果存放在map集合中 让Ajax使用
        Map<String, String> resultMap = new HashMap<String, String>();
        if (attribute == null) {
            //Session失效了，session过期了。验证不通过
            resultMap.put("result", "sessionerror");
        } else if (StringUtils.isNullOrEmpty(oldpassword)) {
            //输入的密码为空，验证不通过
            resultMap.put("result", "error");
        } else {
            String userPassword = attribute.getUserPassword();//Session中用户的密码
            if (oldpassword.equals(userPassword)) {
                //如果旧密码和前端传来的相同，验证通过
                resultMap.put("result", "true");
            } else {
                //旧密码输入错误，验证不通过
                resultMap.put("result", "false");
            }
        }
        //JSONArray 阿里巴巴的JSON工具类 用途就是：转换格式
        return JSONArray.toJSONString(resultMap);
    }

    //查询用户列表    重点、难点
    @RequestMapping(value = "/jsp/user.do", params = {"method=query"})
    public String query(String queryname, String queryUserRole, String pageIndex, Model model){

        int userRole = 0;// 用户角色 默认为0
        //第一次走这个请求，一定是第一页，页面大小固定的
        int pageSize = Constants.Page_Size;//可以把这个写到配置文件中，方便后期修改
        int currentPageNo = 1;

        //根据前端传来的信息做转化 替换默认分页信息
        if (queryname == null){
            queryname = "";
        }
        if (queryUserRole!=null && !queryUserRole.trim().equals("")){
            userRole = Integer.parseInt(queryUserRole);//给查询赋值! 0,1,2,3
        }
        if (pageIndex!=null){
            currentPageNo = Integer.parseInt(pageIndex);
        }

        //获取用户的总数（分页： 上一页、下一页）
        int totalCount = userService.getUserCount(queryname, userRole);
        //总页数支持
        PageSupport pageSupport = new PageSupport();
        pageSupport.setCurrentPageNo(currentPageNo);
        pageSupport.setPageSize(pageSize);
        pageSupport.setTotalCount(totalCount);

        int totalPageCount = ((int)(totalCount/pageSize))+1;

        //控制页面首页和尾页
        //如果页面要小于1，就显示第一页的东西
        if (currentPageNo < 1){
            currentPageNo = 1;
        }else if (currentPageNo>totalPageCount){//如果页面大于总页面数，显示最后一页
            currentPageNo = totalPageCount;
        }
        //获取用户列表展示
        List<User> userList = userService.getUserList(queryname, userRole, (currentPageNo-1)*pageSize, pageSize);
        List<Role> roleList = roleService.getRoleList();

        model.addAttribute("userList",userList);
        model.addAttribute("roleList",roleList);
        model.addAttribute("totalCount",totalCount);
        model.addAttribute("currentPageNo",currentPageNo);
        model.addAttribute("totalPageCount",totalPageCount);
        model.addAttribute("queryUserName",queryname);
        model.addAttribute("queryUserRole",userRole);
        return "userlist";
    }

    //跳转到添加用户界面
    @RequestMapping("/jsp/useradd.do")
    public String toAddUser(){
        return "useradd";
    }

    //增加用户
    @RequestMapping(value = "/jsp/user.do", params = {"method=add"})
    public String addUser(String userCode, String userName, String userPassword, String gender, String birthday, String phone, String address, String userRole, HttpServletRequest req, RedirectAttributes attributes) throws ParseException {
        System.out.println("add()================");
        User user = new User();
        user.setUserCode(userCode);
        user.setUserName(userName);
        user.setUserPassword(userPassword);
        user.setGender(Integer.parseInt(gender));
        user.setBirthday(new SimpleDateFormat("yyyy-MM-DD").parse(birthday));
        user.setPhone(phone);
        user.setAddress(address);
        user.setUserRole(Integer.valueOf(userRole));
        user.setCreationDate(new Date());
        user.setCreatedBy(((User)req.getSession().getAttribute(Constants.USER_SESSION)).getId());
        if (userService.add(user)){
            attributes.addAttribute("method","query");
            return "redirect:/jsp/user.do";//说明执行成功 到用户管理页面
        }else {
            return "useradd";//说明添加失败回到添加页面
        }
    }

    @RequestMapping(value = "/jsp/user.do", params = {"method=getrolelist"})
    @ResponseBody
    public List getRoleList() {
        List<Role> roleList = roleService.getRoleList();
        return roleList;
    }

    @RequestMapping(value = "/jsp/user.do", params = {"method=ucexist"})
    //ajax后台验证userCode是否已存在    /jsp/user.do?method=ucexist
    public void userCodeExist(String userCode, HttpServletResponse resp) throws IOException {
        //判断用户账户是否可用
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if (StringUtils.isNullOrEmpty(userCode)){
            resultMap.put("userCode","exist");
        }else {
            User user = userService.selectUserCodeExist(userCode);
            if (user != null){
                resultMap.put("userCode","exist");
            }else {
                resultMap.put("userCode","unexist");
            }
        }
        //将resultMap转化为JSON形式输出
        resp.setContentType("application/json");//配置上下文的输出类型
        PrintWriter outPrintWriter = resp.getWriter();//从response对象中获取往外输出的writer对象
        outPrintWriter.write(JSONArray.toJSONString(resultMap));//把resultMap转为json字符串 输出
        outPrintWriter.flush();//刷新
        outPrintWriter.close();//关闭流
    }

    //根据userId删除user     "/jsp/user.do?method=deluser"
    @RequestMapping(value = "/jsp/user.do", params = {"method=deluser"})
    public void delUser(String uid, HttpServletResponse resp) throws IOException {
        Integer delId = Integer.parseInt(uid);
        HashMap<String, String> resultMap = new HashMap<String, String>();

        if (delId <= 0){
            resultMap.put("delResult","notexist");
        }else {
            if (userService.deleteUserById(delId)){
                resultMap.put("delResult","true");
            }else {
                resultMap.put("delResult","false");
            }
        }
        //将resultMap转化为JSON形式输出
        //配置上下文的输出类型
        resp.setContentType("application/json");
        //从response对象中获取往外输出的writer对象
        PrintWriter outPrintWriter = resp.getWriter();
        //把resultMap转为json字符串 输出
        outPrintWriter.write(JSONArray.toJSONString(resultMap));
        outPrintWriter.flush();//刷新
        outPrintWriter.close();//关闭流
    }

    //通过userId获取user信息 用户管理模块中的子模块：查看用户信息
    @RequestMapping(value = "/jsp/user.do", params = {"method=view"})
    public String getUserById(String uid, Model model){
        //调用后台方法得到user对象
        User user = userService.getUserById(Integer.parseInt(uid));
        model.addAttribute("user",user);
        return "userview";
    }

    //通过userId获取user信息 用户管理模块中的子模块：修改用户（获取用户信息后跳转到用户修改界面）
    @RequestMapping(value = "/jsp/user.do", params = {"method=modify"})
    public String toModify(String uid, Model model){
        //调用后台方法得到user对象
        User user = userService.getUserById(Integer.parseInt(uid));
        model.addAttribute("user",user);
        return "usermodify";
    }

    //修改用户信息  用户管理模块中的子模块：修改用户
    @RequestMapping(value = "/jsp/user.do", params = {"method=modifyexe"})
    public String modify(String uid, String userName, String gender, String birthday, String phone, String address, String userRole, RedirectAttributes attributes, HttpSession session) throws ParseException {
        User user = new User();
        user.setId(Integer.valueOf(uid));
        user.setUserName(userName);
        user.setGender(Integer.valueOf(gender));
        user.setBirthday(new SimpleDateFormat("yyyy-MM-DD").parse(birthday));
        user.setPhone(phone);
        user.setAddress(address);
        user.setUserRole(Integer.valueOf(userRole));
        user.setModifyDate(new Date());
        user.setModifyBy(((User)session.getAttribute(USER_SESSION)).getId());

        if (userService.modify(user)) {
            attributes.addAttribute("method","query");
            return "redirect:/jsp/user.do";//说明执行成功 到用户管理页面
        }else {
            return "usermodify";
        }
    }

}
