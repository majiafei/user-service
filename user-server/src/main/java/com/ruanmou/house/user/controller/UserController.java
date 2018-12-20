package com.ruanmou.house.user.controller;

import com.google.common.collect.Lists;
import com.ruanmou.house.common.ApiResponse;
import com.ruanmou.house.user.constant.UserConstant;
import com.ruanmou.house.user.constant.UserEnum;
import com.ruanmou.house.user.domain.User;
import com.ruanmou.house.user.exception.UserException;
import com.ruanmou.house.user.service.UserService;
import com.ruanmou.house.user.service.impl.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @ProjectName: house
 * @Package: com.ruanmou.house.user.controller
 * @ClassName: UserController
 * @Author: majiafei
 * @Description:
 * @Date: 2018/12/18 11:13
 */
@Controller
@Api(value = "用户相关的接口")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    /**
     * 用户的登录
     * @param user
     * @return
     */
    @PostMapping("/accounts/signin")
    public String login(User user, HttpSession session) {
        user = userService.login(user.getEmail(), user.getPassword());
        if (user != null) {
            session.setAttribute("userName", user.getName());
            session.setAttribute("type", user.getType());
            return "index";
        }

        throw new UserException(UserException.UserType.USER_NOT_LOGIN, "请重新登录");
    }

    @GetMapping("/get")
    public ApiResponse get(String token) {
        User user = userService.getLoginUserByToken(token);
        return ApiResponse.ofSuccess(user);
    }

    @GetMapping("/logout")
    public ApiResponse logout(String token) {
        userService.logout(token);
        return ApiResponse.ofSuccess(null);
    }

    @PostMapping("/accounts/register")
    public String regiter(User user, HttpServletRequest request, Model model) {
        try {
           // 截取ip地址和端口号
            String verifyUrl = StringUtils.substringBefore(request.getRequestURL().toString(), request.getRequestURI());
            // 上传图片
            String imagPath = fileService.getImgPath(Lists.newArrayList(user.getAvatarFile())).get(0);
            user.setAvatar(imagPath);
            // 未激活
            user.setEnable(UserEnum.DISABLE.getCode());
            userService.addAccount(user, verifyUrl + UserConstant.ACTIVE_URL);
        }catch (Exception e) {
            e.printStackTrace();
            return "redirect:/account/register";
        }
        model.addAttribute("email", user.getEmail());
        return "accounts/registerSubmit";
    }

    /**
     * 认证
     * @param key
     * @param model
     * @return
     */
    @GetMapping("account/verify")
    public String verify(String key, Model model) {
        try {
            userService.enableAccount(key);
        }catch (Exception e) {
            model.addAttribute("message", "token失效，请重新注册");
            return "";
        }
        return "redirect:/account/login";
    }

    @GetMapping("/profile")
    public String profile() {
        return "accounts/profile";
    }

}
