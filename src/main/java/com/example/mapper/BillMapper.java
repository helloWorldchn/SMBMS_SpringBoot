package com.example.mapper;

import com.example.pojo.Bill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

//这个注解表示了这是一个mybatis的Mapper类
@Mapper
@Repository
public interface BillMapper {

    //根据商品名称、供应商id、是否付款 查询订单总数，以实现订单列表分页功能
    int getBillCount(@Param("productName") String productName, @Param("providerId") int providerId, @Param("isPayment") int isPayment);
    // 通过查询条件获取供应商列表-模糊查询-getBillList
    List<Bill> getBillList(@Param("productName") String productName, @Param("providerId") int providerId, @Param("isPayment") int isPayment);

    //增加订单
    int add(Bill bill);

    //通过delId删除Bill
    int deleteBillById(int delId);

    //通过billId获取Bill
    Bill getBillById(int id);

    //修改订单信息
    int modify(Bill bill);

    //根据供应商ID查询订单数量
    //int getBillCountByProviderId(String providerId);
}
