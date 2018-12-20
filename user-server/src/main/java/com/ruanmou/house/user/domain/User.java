package com.ruanmou.house.user.domain;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

/**
 * 用户实体类
 * @ProjectName: house
 * @Package: com.ruanmou.house.user.domain
 * @ClassName: User
 * @Author: majiafei
 * @Description:
 * @Date: 2018/12/18 10:35
 */
@Data
public class User {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    private String  name;
    private String  phone;
    private String  email;
    private String  aboutme;
    private String  password;
    private String avatar;
    private Integer type;
    private Date createTime;
    private Integer enable;

    @Transient
    private String  confirmPassword;
    @Transient
    private String token;
    @Transient // 激活链接
    private String enableUrl;
    @Transient
    private MultipartFile avatarFile;
}
