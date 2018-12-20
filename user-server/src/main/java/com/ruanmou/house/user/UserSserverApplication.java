package com.ruanmou.house.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @ProjectName: house
 * @Package: com.ruanmou.house.user
 * @ClassName: UserSserverApplication
 * @Author: majiafei
 * @Description:
 * @Date: 2018/12/18 10:24
 */
@SpringBootApplication(scanBasePackages = "com.ruanmou.house")
@MapperScan(basePackages = "com.ruanmou.house.user.mapper")
@EnableAsync
public class UserSserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserSserverApplication.class, args);
    }

}
