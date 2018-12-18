package com.ruanmou.house.user.service;

import com.ruanmou.house.user.domain.User;

import java.util.List;

/**
 * 用户服务
 * @ProjectName: house
 * @Package: com.ruanmou.house.user.service
 * @ClassName: UserService
 * @Author: majiafei
 * @Description:
 * @Date: 2018/12/18 10:40
 */
public interface UserService {

    /**
     * 根据用户id查询用户
     * @param userId  用户id
     * @return
     */
    User getUserById(Integer userId);

    /**
     * 根据条件去查询用户列表
     * @param user 用户对象
     * @return
     */
    List<User>  getUserListByCondition(User user);

    /**
     * 用户的注册
     * @param user 用户实体对象
     * @param enabelUrl  激活的url
     * @return
     */
    boolean addAccount(User user, String enabelUrl);

    /**
     * 激活用户
     * @param key
     * @return
     */
    boolean enableAccount(String key);

}
