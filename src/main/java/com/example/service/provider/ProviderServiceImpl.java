package com.example.service.provider;

import com.example.mapper.ProviderMapper;
import com.example.pojo.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProviderServiceImpl implements ProviderService{
    //service层调dao层，组合Dao
    @Autowired
    private ProviderMapper providerMapper;
    public void setProviderMapper(ProviderMapper providerMapper) {
        this.providerMapper = providerMapper;
    }

    //查询记录数，以实现供应商列表分页功能
    public int getProviderCount(String proName, String proCode) {
        return providerMapper.getProviderCount(proName, proCode);
    }
    //通过供应商名称、编码获取供应商列表-模糊查询-providerList
    public List<Provider> getProviderList(String proName, String proCode) {
        return providerMapper.getProviderList(proName, proCode);
    }

    public boolean add(Provider provider) {
        boolean flag = false;
        int addRows = providerMapper.add(provider);
        if (addRows > 0){
            flag = true;
        } else {
            flag = false;
        }
        return flag;
    }

    //通过proId删除Provider
    public boolean deleteProviderById(Integer delId) {
        boolean flag = false;
        int delRows = providerMapper.deleteProviderById(delId);
        if (delRows > 0){
            flag = true;
        } else {
            flag = false;
        }
        return flag;
    }

    //通过proId获取Provider
    public Provider getProviderById(int id) {
        return providerMapper.getProviderById(id);
    }

    //修改用户信息
    public boolean modify(Provider provider) {
        boolean flag = false;
        int modifyRows = providerMapper.modify(provider);
        if (modifyRows > 0){
            flag = true;
        } else {
            flag = false;
        }
        return flag;
    }

}
