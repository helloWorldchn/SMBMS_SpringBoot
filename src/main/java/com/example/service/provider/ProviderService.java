package com.example.service.provider;

import com.example.pojo.Provider;

import java.util.List;

public interface ProviderService {

    //查询记录数，以实现供应商列表分页功能
    int getProviderCount(String proName,String proCode);
    //通过供应商名称、编码获取供应商列表-模糊查询-providerList
    List<Provider> getProviderList(String proName, String proCode);//, int currentPageNo, int pageSize

    //增加供应商
    boolean add(Provider provider);

    //通过proId删除Provider
    boolean deleteProviderById(Integer delId);

    //通过proId获取Provider
    Provider getProviderById(int id);

    //修改用户信息
    boolean modify(Provider provider);

}
