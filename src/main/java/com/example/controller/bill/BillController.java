package com.example.controller.bill;

import com.alibaba.fastjson.JSONArray;
import com.example.pojo.Bill;
import com.example.pojo.Provider;
import com.example.pojo.User;
import com.example.service.bill.BillService;
import com.example.service.provider.ProviderService;
import com.example.util.Constants;
import com.mysql.jdbc.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
public class BillController {
    @Autowired
    private BillService billService;

    @Autowired
    private ProviderService providerService;

    //@RequestMapping(value = "/jsp/bill.do", params = {"method=getproviderlist"})
    ////获取供应商列表（添加和修改bill时的下拉框使用）
    //private void getProviderList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    //    System.out.println("getproviderlist ========================= ");
    //    List<Provider> providerList = providerService.getProviderList("","");
    //    //把providerList转换成json对象输出
    //    resp.setContentType("application/json");
    //    PrintWriter outPrintWriter = resp.getWriter();
    //    outPrintWriter.write(JSONArray.toJSONString(providerList));
    //    outPrintWriter.flush();
    //    outPrintWriter.close();
    //}
    @RequestMapping(value = "/jsp/bill.do", params = {"method=getproviderlist"})
    @ResponseBody
    //获取供应商列表（添加和修改bill时的下拉框使用）
    private List getProviderList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Provider> providerList = providerService.getProviderList("","");
        return providerList;
    }

    @RequestMapping(value = "/jsp/bill.do", params = {"method=query"})
    // 通过查询条件获取供应商列表-模糊查询-getBillList
    public String query(String queryProductName, String queryProviderId, String queryIsPayment, HttpServletRequest req, Model model) {
        List<Provider> providerList = providerService.getProviderList("","");
        model.addAttribute("providerList", providerList);

        if(StringUtils.isNullOrEmpty(queryProductName)){
            queryProductName = "";
        }
        int billProviderId;
        if(StringUtils.isNullOrEmpty(queryProviderId)){
            billProviderId= 0;
        }else{
            billProviderId =  Integer.parseInt(queryProviderId);
        }
        int billIsPayment;
        if(StringUtils.isNullOrEmpty(queryIsPayment)){
            billIsPayment = 0;
        }else{
            billIsPayment = Integer.parseInt(queryIsPayment);
        }

        //int total = billService.getBillCount(queryProductName, billProviderId, billIsPayment);

        List<Bill> billList = billService.getBillList(queryProductName, billProviderId, billIsPayment);
        model.addAttribute("billList", billList);
        model.addAttribute("queryProductName", queryProductName);
        model.addAttribute("queryProviderId", queryProviderId);
        model.addAttribute("queryIsPayment", queryIsPayment);
        return "billlist";
    }

    //跳转到添加用户界面
    @RequestMapping("/jsp/billadd.do")
    public String toAddBill(){
        return "billadd";
    }

    @RequestMapping(value = "/jsp/bill.do", params = {"method=add"})
    //增加用户
    public String add(String billCode, String productName, String productDesc, String productUnit, String productCount, String totalPrice, String providerId, String isPayment, HttpServletRequest req, RedirectAttributes attributes) {

        Bill bill = new Bill();
        bill.setBillCode(billCode);
        bill.setProductName(productName);
        bill.setProductDesc(productDesc);
        bill.setProductUnit(productUnit);
        bill.setProductCount(new BigDecimal(productCount).setScale(2,BigDecimal.ROUND_DOWN));
        bill.setIsPayment(Integer.parseInt(isPayment));
        bill.setTotalPrice(new BigDecimal(totalPrice).setScale(2,BigDecimal.ROUND_DOWN));
        bill.setProviderId(Integer.parseInt(providerId));
        bill.setCreatedBy(((User)req.getSession().getAttribute(Constants.USER_SESSION)).getId());
        bill.setCreationDate(new Date());

        if (billService.add(bill)){
            attributes.addAttribute("method","query");
            return "redirect:/jsp/bill.do";//说明执行成功 到订单管理页面
        }else {
            return "billadd";
        }
    }

    @RequestMapping(value = "/jsp/bill.do", params = {"method=delbill"})
    //通过delId删除Bill
    public void delBill(String billid, HttpServletResponse resp) throws IOException {
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if(!StringUtils.isNullOrEmpty(billid)){
            boolean flag = billService.deleteBillById(Integer.parseInt(billid));
            if(flag){//删除成功
                resultMap.put("delResult", "true");
            }else{//删除失败
                resultMap.put("delResult", "false");
            }
        }else{
            resultMap.put("delResult", "notexit");
        }
        //把resultMap转换成json对象输出
        resp.setContentType("application/json");
        PrintWriter outPrintWriter = resp.getWriter();
        outPrintWriter.write(JSONArray.toJSONString(resultMap));
        outPrintWriter.flush();
        outPrintWriter.close();
    }


    @RequestMapping(value = "/jsp/bill.do", params = {"method=view"})
    //通过billId获取bill 订单管理模块中的子模块：查看订单
    public String getBillById(String billid, Model model) {
        Bill bill = billService.getBillById(Integer.parseInt(billid));
        model.addAttribute("bill",bill);
        return "billview";
    }

    //通过billId获取bill信息 订单管理模块中的子模块：修改订单（获取订单信息后跳转到订单修改界面）
    @RequestMapping(value = "/jsp/bill.do", params = {"method=modify"})
    public String toModify(String billid, Model model){
        Bill bill = billService.getBillById(Integer.parseInt(billid));
        model.addAttribute("bill",bill);
        return "billmodify";
    }

    //修改订单信息
    @RequestMapping(value = "/jsp/bill.do", params = {"method=modifysave"})
    public String modify(String id, String productName, String productDesc, String productUnit, String productCount, String totalPrice, String providerId, String isPayment, HttpServletRequest req, RedirectAttributes attributes){
        Bill bill = new Bill();
        bill.setId(Integer.valueOf(id));
        bill.setProductName(productName);
        bill.setProductDesc(productDesc);
        bill.setProductUnit(productUnit);
        bill.setProductCount(new BigDecimal(productCount).setScale(2,BigDecimal.ROUND_DOWN));
        bill.setIsPayment(Integer.parseInt(isPayment));
        bill.setTotalPrice(new BigDecimal(totalPrice).setScale(2,BigDecimal.ROUND_DOWN));
        bill.setProviderId(Integer.parseInt(providerId));
        bill.setModifyBy(((User)req.getSession().getAttribute(Constants.USER_SESSION)).getId());
        bill.setModifyDate(new Date());

        if (billService.modify(bill)){
            attributes.addAttribute("method","query");
            return "redirect:/jsp/bill.do";//说明执行成功 到用户管理页面
        } else {
            return "billmodify";
        }
    }

}
