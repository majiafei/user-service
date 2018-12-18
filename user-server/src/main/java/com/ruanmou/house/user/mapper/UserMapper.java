package com.ruanmou.house.user.mapper;

import com.ruanmou.house.user.common.HouseBaseMapper;
import com.ruanmou.house.user.domain.User;

/**
 * @ProjectName: house
 * @Package: com.ruanmou.house.user.mapper
 * @ClassName: UserMapper
 * @Author: majiafei
 * @Description:
 * @Date: 2018/12/18 10:39
 */
public interface UserMapper extends HouseBaseMapper<User> {

    /**
     * 根据条件更新用户
     * @param user
     */
    void updateUser(User user);

}
