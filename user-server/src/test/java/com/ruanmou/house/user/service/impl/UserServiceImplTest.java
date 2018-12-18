package com.ruanmou.house.user.service.impl;

import com.ruanmou.house.user.domain.User;
import com.ruanmou.house.user.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @ProjectName: house
 * @Package: com.ruanmou.house.user.service.impl
 * @ClassName: UserServiceImplTest
 * @Author: majiafei
 * @Description:
 * @Date: 2018/12/18 11:14
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Test
    public void test() {
        User user = userService.getUserById(7);
        System.out.println(user.getAvatar());
    }

    @Test
    public void testList() {
        User user = new User();
        user.setName("hello1");
        List<User> userList = userService.getUserListByCondition(user);
        System.out.println(userList.size());
    }

    @Test
    public void testAddAccount() {
        User user = userService.getUserById(8);
        user.setEmail("1571221458@qq.com");
        user.setId(null);
        userService.addAccount(user, "http://user/notify");
    }

}