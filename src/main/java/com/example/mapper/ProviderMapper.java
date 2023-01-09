package com.example.mapper;

import com.example.pojo.Provider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

//这个注解表示了这是一个mybatis的Mapper类
@Mapper
@Repository
public interface ProviderMapper {

    //查询用户总数，以实现供应商列表分页功能
    int getProviderCount(@Param("proName") String proName, @Param("proCode") String proCode);
    //通过供应商名称、编码获取供应商列表-模糊查询-providerList
    List<Provider> getProviderList(@Param("proName") String proName, @Param("proCode") String proCode);//, @Param("currentPageNo") int currentPageNo, @Param("pageSize") int pageSize

    //增加供应商 供应商管理模块中的子模块：添加供应商
    int add(Provider provider);

    //通过proId删除Provider
    int deleteProviderById(Integer delId);

    //通过proId获取Provider
    Provider getProviderById(int id);

    //修改用户信息
    int modify(Provider provider);
}
