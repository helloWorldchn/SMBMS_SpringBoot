package com.example.controller.provider;

import com.alibaba.fastjson.JSONArray;
import com.example.pojo.Provider;
import com.example.pojo.User;
import com.example.service.provider.ProviderService;
import com.example.util.Constants;
import com.mysql.jdbc.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.example.util.Constants.USER_SESSION;

@Controller
public class ProviderController {

    @Autowired
    private ProviderService providerService;

    //通过供应商名称、编码获取供应商列表-模糊查询-providerList
    @RequestMapping(value = "/jsp/provider.do", params = {"method=query"})
    public String query(String queryProName, String queryProCode, String pageIndex, HttpServletRequest req, Model model) {
        //int pageSize = Constants.Page_Size;//可以把这个写到配置文件中，方便后期修改
        //int currentPageNo = 1;

        if (StringUtils.isNullOrEmpty(queryProName)){
            queryProName = "";
        }
        if (StringUtils.isNullOrEmpty(queryProCode)){
            queryProCode = "";
        }
        //if (pageIndex!=null){
        //    currentPageNo = Integer.parseInt(pageIndex);
        //}
        ////获取用户的总数（分页： 上一页、下一页）
        //int totalCount = providerService.getProviderCount(queryProName, queryProCode);
        ////总页数支持
        //PageSupport pageSupport = new PageSupport();
        //pageSupport.setCurrentPageNo(currentPageNo);
        //pageSupport.setPageSize(pageSize);
        //pageSupport.setTotalCount(totalCount);
        //
        //int totalPageCount = ((int)(totalCount/pageSize))+1;
        //
        ////控制页面首页和尾页
        ////如果页面要小于1，就显示第一页的东西
        //if (currentPageNo < 1){
        //    currentPageNo = 1;
        //}else if (currentPageNo>totalPageCount){//如果页面大于总页面数，显示最后一页
        //    currentPageNo = totalPageCount;
        //}
        List<Provider> providerList = providerService.getProviderList(queryProName, queryProCode);//, (currentPageNo-1)*pageSize, pageSize
        model.addAttribute("providerList",providerList);
        model.addAttribute("queryProName", queryProName);
        model.addAttribute("queryProCode", queryProCode);
        //model.addAttribute("totalCount",totalCount);
        //model.addAttribute("currentPageNo",currentPageNo);
        //model.addAttribute("totalPageCount",totalPageCount);
        return "providerlist";
    }

    //跳转到添加供应商界面
    @RequestMapping("/jsp/provideradd.do")
    public String toAddProvider(){
        return "provideradd";
    }

    @RequestMapping(value = "/jsp/provider.do", params = {"method=add"})
    //增加供应商
    public String add(String proCode, String proName, String proContact, String proPhone, String proAddress, String proFax, String proDesc, HttpServletRequest req, RedirectAttributes attributes) {
        Provider provider = new Provider();
        provider.setProCode(proCode);
        provider.setProName(proName);
        provider.setProContact(proContact);
        provider.setProPhone(proPhone);
        provider.setProFax(proFax);
        provider.setProAddress(proAddress);
        provider.setProDesc(proDesc);
        provider.setCreationDate(new Date());
        provider.setCreatedBy(((User)req.getSession().getAttribute(Constants.USER_SESSION)).getId());

        if(providerService.add(provider)){
            attributes.addAttribute("method","query");
            return "redirect:/jsp/provider.do";//说明执行成功 到供应商管理页面
        } else {
            return "provideradd";//说明添加失败回到添加页面
        }
    }

    //通过proId删除Provider    "/jsp/provider.do?method=deluser"
    @RequestMapping(value = "/jsp/provider.do", params = {"method=delprovider"})
    public void deleteProvider(String proid, HttpServletResponse resp) throws IOException {
        Integer delId = Integer.parseInt(proid);
        HashMap<String, String> resultMap = new HashMap<String, String>();

        if (delId <= 0){
            resultMap.put("delResult","notexist");
        }else {
            if (providerService.deleteProviderById(delId)){
                resultMap.put("delResult","true");
            }else {
                resultMap.put("delResult","false");
            }
        }
        //将resultMap转化为JSON形式输出
        //配置上下文的输出类型
        resp.setContentType("application/json");
        //从response对象中获取往外输出的writer对象
        PrintWriter outPrintWriter = resp.getWriter();
        //把resultMap转为json字符串 输出
        outPrintWriter.write(JSONArray.toJSONString(resultMap));
        outPrintWriter.flush();//刷新
        outPrintWriter.close();//关闭流
    }

    //通过proId获取Provider 供应商管理模块中的子模块：查看供应商
    @RequestMapping(value = "/jsp/provider.do", params = {"method=view"})
    public String getProviderById(String proid, Model model){
        Provider provider = providerService.getProviderById(Integer.parseInt(proid));
        model.addAttribute("provider",provider);
        return "providerview";
    }

    //通过proid获取provider信息 供应商管理模块中的子模块：修改供应商（获取供应商信息后跳转到供应商修改界面）
    @RequestMapping(value = "/jsp/provider.do", params = {"method=modify"})
    public String toModify(String proid, Model model){
        Provider provider = providerService.getProviderById(Integer.parseInt(proid));
        model.addAttribute("provider",provider);
        return "providermodify";
    }

    //修改供应商信息  供应商管理模块中的子模块：修改供应商
    @RequestMapping(value = "/jsp/provider.do", params = {"method=modifysave"})
    public String modify(String id, String proName, String proContact, String proPhone, String proAddress, String proFax, String proDesc,HttpServletRequest req, RedirectAttributes attributes) {
        Provider provider = new Provider();
        provider.setId(Integer.valueOf(id));
        provider.setProName(proName);
        provider.setProContact(proContact);
        provider.setProPhone(proPhone);
        provider.setProFax(proFax);
        provider.setProAddress(proAddress);
        provider.setProDesc(proDesc);
        provider.setModifyDate(new Date());
        provider.setModifyBy(((User)req.getSession().getAttribute(Constants.USER_SESSION)).getId());

        if (providerService.modify(provider)) {
            attributes.addAttribute("method","query");
            return "redirect:/jsp/provider.do";//说明执行成功 到用户管理页面
        }else {
            return "providermodify";
        }
    }

}
