package com.example.mapper;

import com.example.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

//这个注解表示了这是一个mybatis的Mapper类
@Mapper
@Repository
public interface UserMapper {
    //得到要登录的用户
    User getLoginUser(String userCode);

    //修改当前用户密码
    int updatePwd(@Param("id") int id, @Param("password") String password);

    //查询用户总数
    int getUserCount(@Param("username") String username, @Param("userRole") int userRole);
    //获取用户列表  通过条件查询userList
    List<User> getUserList(@Param("queryUserName") String queryUserName, @Param("queryUserRole") int queryUserRole, @Param("currentPageNo") int currentPageNo, @Param("pageSize") int pageSize);

    //增加用户信息 用户管理模块中的子模块：添加用户
    int add(User user);

    //通过userId删除user  用户管理模块中的子模块：删除用户
    int deleteUserById(Integer delId);

    //通过userId获取user 用户管理模块中的子模块：查看用户信息（修改用户模块也需要）
    User getUserById(int id);

    //修改用户信息 用户管理模块中的子模块：修改用户
    int modify(User user);
}
