package com.ruanmou.house.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ProjectName: house
 * @Package: com.ruanmou.house.user.controller
 * @ClassName: IndexController
 * @Author: majiafei
 * @Description: 页面跳转
 * @Date: 2018/12/19 10:32
 */
@Controller
public class IndexController {

    @RequestMapping("/account/register")
    public String register() {
        return "accounts/register";
    }

    /**
     * 登录界面
     * @return
     */
    @RequestMapping("/account/login")
    public String login() {
        return "accounts/signin.html";
    }

}
