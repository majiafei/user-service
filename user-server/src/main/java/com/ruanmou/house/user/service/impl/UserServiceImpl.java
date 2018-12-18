package com.ruanmou.house.user.service.impl;

import com.ruanmou.house.user.domain.User;
import com.ruanmou.house.user.mapper.UserMapper;
import com.ruanmou.house.user.service.MailService;
import com.ruanmou.house.user.service.UserService;
import com.ruanmou.house.utils.BeanHelper;
import com.ruanmou.house.utils.HashUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 用户服务的实现
 * @ProjectName: house
 * @Package: com.ruanmou.house.user.service.impl
 * @ClassName: UserServiceImpl
 * @Author: majiafei
 * @Description:
 * @Date: 2018/12/18 10:42
 */
@Service
public class UserServiceImpl implements UserService {

    private static final String USER_KEY_PREFIX = "user";

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MailService mailService;

    // 图片服务器地主
    @Value("${image.file.server}")
    private String imagePrefix;

    /**
     * 根据用户id查询唯一的用户
     * @param userId  用户id
     * @return
     */
    @Override
    public User getUserById(Integer userId) {
        String userKey = USER_KEY_PREFIX  + userId;
        // 根据key查询缓存中的数据
        User user = (User)redisTemplate.opsForValue().get(USER_KEY_PREFIX + userId);
        // 如果该user不存在,就查询数据库,然后放到缓存中
        if (user == null) {
            // 根据主键获取user
            user = userMapper.selectByPrimaryKey(userId);
            // 将该用户放到缓存中去
            redisTemplate.opsForValue().set(userKey, user);
            // 设置过期时间
            redisTemplate.expire(userKey, 5, TimeUnit.MINUTES);

            return user;
        }

        // 否则直接返回
        return  user;
    }

    /**
     * 根据用户的某些信息查询用户列表
     * @param user 用户对象
     * @return
     */
    @Override
    public List<User> getUserListByCondition(User user) {
        // 根据条件查询用户的列表
        List<User> userList = userMapper.select(user);
        // 设置图片的地址
        userList.forEach(u -> u.setAvatar(imagePrefix + u.getAvatar()));
        return userList;
    }

    /**
     * 用户的注册
     * @param user 用户实体对象
     * @param enabelUrl  激活的url
     * @return
     */
    @Override
    public boolean addAccount(User user, String enabelUrl) {
        // 将密码加密
        HashUtils.encryPassword(user.getPassword());
        // 设置时间
        BeanHelper.onInsert(user);
        // 插入数据
        userMapper.insert(user);

        // 发送邮件
        registerNotify(user.getEmail(), enabelUrl);

        return true;
    }

    /**
     * 激活用户: 修改激活状态
     * @param key
     * @return
     */
    @Override
    public boolean enableAccount(String key) {
        String email = (String)redisTemplate.opsForValue().get(key);
        if (email == null) {
            throw new RuntimeException("邮件不合法，key无效");
        }
        User user = new User();
        user.setEmail(email);
        user.setEnable(1);
        // 更新用户信息
        userMapper.updateUser(user);
        return false;
    }

    private void registerNotify(String email, String enabelUrl) {
        // 生成随机字符串
        String randomKey = HashUtils.hashString(enabelUrl) + RandomStringUtils.randomAlphanumeric(10);
        // 将随机字符串放到redis中
        redisTemplate.opsForValue().set(randomKey, email);
        // 设置过期时间
        redisTemplate.expire(randomKey, 30, TimeUnit.MINUTES);
        // 邮件内容
        String content = enabelUrl + "?key=" + randomKey;
        // 发送邮件
        mailService.send("发送激活链接", content, email);
    }
}
