package com.example.service.user;

import com.example.pojo.User;

import java.util.List;

public interface UserService {
    //用户登录
    User login(String userCode, String password);

    //修改当前用户密码
    boolean updatePwd(int id, String password);

    //查询记录数 用户管理模块 用户列表
    int getUserCount(String username,int userRole);
    //根据条件查询用户列表
    List<User> getUserList(String queryUserName, int queryUserRole, int currentPageNo, int pageSize);

    //增加用户 用户管理模块中的子模块：添加用户
    boolean add(User user);
    //通过userCode查询User
    User selectUserCodeExist(String userCode);

    //通过userId删除user  用户管理模块中的子模块：删除用户
    boolean deleteUserById(Integer delId);

    //通过userId获取user信息 用户管理模块中的子模块：查看用户信息（修改用户模块也需要）
    User getUserById(int id);

    //修改用户信息  用户管理模块中的子模块：修改用户
    boolean modify(User user);

}
