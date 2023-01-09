package com.example;

import com.example.pojo.Bill;
import com.example.pojo.Provider;
import com.example.pojo.Role;
import com.example.pojo.User;
import com.example.service.bill.BillService;
import com.example.service.provider.ProviderService;
import com.example.service.role.RoleService;
import com.example.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@SpringBootTest
class SMBMSSpringBootApplicationTests {

    @Autowired
    UserService userService;
    @Autowired
    BillService billService;
    @Autowired
    RoleService roleService;
    @Autowired
    ProviderService providerService;

    @Test
    void contextLoads() {
        User userById = userService.getUserById(1);
        System.out.println("userById->"+userById);
        List<User> userList = userService.getUserList(null,0,1,15);
        for (int i = 0; i < userList.size(); i++) {
            System.out.println(userList.get(i).getUserName());
        }
        System.out.println("==========================");

        List<Role> roleList = roleService.getRoleList();
        for (int i = 0; i < roleList.size(); i++) {
            System.out.println(roleList.get(i).getRoleName());
        }
        System.out.println("==========================");

        Bill billById = billService.getBillById(2);
        System.out.println("billById->"+billById.getBillCode());
        List<Bill> billList = billService.getBillList(null, 0, 0);
        for (Bill bill : billList) {
            System.out.println(bill.getBillCode());
        }
        System.out.println("==========================");

        Provider providerById = providerService.getProviderById(1);
        System.out.println("providerById->"+providerById.getProCode());
        List<Provider> providerList = providerService.getProviderList(null, null);
        for (Provider provider : providerList) {
            System.out.println(provider.getProCode());
        }
    }

}
