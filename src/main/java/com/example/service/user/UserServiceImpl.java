package com.example.service.user;

import com.example.mapper.UserMapper;
import com.example.pojo.User;
import com.example.util.DateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{
    //service层调dao层，组合Dao
    @Autowired
    private UserMapper userMapper;
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    //用户登录
    public User login(String userCode, String password) {
        return userMapper.getLoginUser(userCode);
    }

    //修改当前用户密码
    public boolean updatePwd(int id, String password) {
        boolean flag = false;
        if (userMapper.updatePwd(id, password) > 0){
            flag = true;
        }
        return flag;
    }

    public int getUserCount(String username, int userRole) {
        return userMapper.getUserCount(username, userRole);
    }

    public List<User> getUserList(String queryUserName, int queryUserRole, int currentPageNo, int pageSize) {
        return userMapper.getUserList(queryUserName, queryUserRole, currentPageNo, pageSize);
    }

    //增加用户
    public boolean add(User user) {
        boolean flag = false;
        if (userMapper.add(user) > 0){
            flag = true;
        }
        return flag;
    }

    //通过userCode查询User
    public User selectUserCodeExist(String userCode) {
        return userMapper.getLoginUser(userCode);
    }

    //通过userId删除user
    public boolean deleteUserById(Integer delId) {
        boolean flag = false;
        if (userMapper.deleteUserById(delId) > 0){
            flag = true;
        }
        return flag;
    }

    //通过userId获取user信息
    public User getUserById(int id) {
        User user = userMapper.getUserById(id);
        //对birthday的格式进行修改  将Wed Apr 22 05:12:10 CST 1989格式的java.util.Date转为1989-04-22格式的java.sql.Date
        user.setBirthday(new DateFormat().DateToDate(user.getBirthday()));
        return user;
    }

    //修改用户信息
    public boolean modify(User user) {
        boolean flag = false;
        if (userMapper.modify(user) > 0){
            flag = true;
        }
        return flag;
    }
}
