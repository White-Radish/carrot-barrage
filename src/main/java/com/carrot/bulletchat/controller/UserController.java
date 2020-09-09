package com.carrot.bulletchat.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.carrot.bulletchat.domain.User;
import com.carrot.bulletchat.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author carrot
 * @date 2020/4/17 12:00
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService iUserService;
    @RequestMapping(value = "/resetpsswd",method = RequestMethod.POST)
    public String resetPasswd(){

        try {
            iUserService.resetPassWd();
            return "success";
        } catch (Exception e) {
            e.printStackTrace();

        }
            return new String();
    }
    @RequestMapping(value = "/getUser/{id}",method = RequestMethod.GET)
    public User getUser(@PathVariable(value = "id") Long id){

        try {
//            iUserService.getUser(id);
            return iUserService.selectById(id);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return new User();
    }

    @RequestMapping(value = "/getUserList",method = RequestMethod.GET)
    public List<User> getUserList(){

        try {
            Page page=new Page(0,3);
            return iUserService.getUserListByPage(page);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return new ArrayList<User>();
    }

}
