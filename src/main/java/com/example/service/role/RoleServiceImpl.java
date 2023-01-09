package com.example.service.role;

import com.example.mapper.RoleMapper;
import com.example.pojo.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService{

    //service层调dao层，组合Dao
    @Autowired
    private RoleMapper roleMapper;
    public void setRoleMapper(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    public List<Role> getRoleList() {
        return roleMapper.getRoleList();
    }

}
