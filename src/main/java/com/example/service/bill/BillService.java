package com.example.service.bill;

import com.example.pojo.Bill;

import java.util.List;

public interface BillService {

    //查询记录数，以实现订单列表分页功能
    int getBillCount(String productName, int providerId, int isPayment);
    //通过商品名称、供应商id、是否付款查询订单列表-模糊查询-billList
    List<Bill> getBillList(String productName, int providerId, int isPayment);

    //增加订单
    boolean add(Bill bill);

    //通过delId删除Bill
    boolean deleteBillById(int delId);

    //通过billId获取Bill
    Bill getBillById(int id);

    //修改订单信息
    boolean modify(Bill bill);

}
