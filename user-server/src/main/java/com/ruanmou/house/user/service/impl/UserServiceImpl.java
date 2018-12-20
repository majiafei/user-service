package com.ruanmou.house.user.service.impl;

import com.google.common.collect.ImmutableMap;
import com.ruanmou.house.user.constant.UserEnum;
import com.ruanmou.house.user.domain.User;
import com.ruanmou.house.user.exception.UserException;
import com.ruanmou.house.user.mapper.UserMapper;
import com.ruanmou.house.user.service.MailService;
import com.ruanmou.house.user.service.UserService;
import com.ruanmou.house.utils.BeanHelper;
import com.ruanmou.house.utils.HashUtils;
import com.ruanmou.house.utils.JwtHelper;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.util.List;
import java.util.Map;
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
        user.setPassword(HashUtils.encryPassword(user.getPassword()));
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

    /**
     * 用户的登录
     * @param email 邮箱
     * @param password 密码
     * @return
     */
    @Override
    public User login(String email, String password) {
        // 校验账户和密码
        if (StringUtils.isBlank(email) || StringUtils.isBlank(password)) {
            throw new UserException(UserException.UserType.ACCOUNT_AND_PASSWORD_IS_NULL, "邮箱和密码是必填的");
        }

        // 构造对象设置条件
        User user = new User();
        user.setEnable(UserEnum.ACTIVE.getCode());
        user.setPassword(HashUtils.encryPassword(password));
        user.setEmail(email);

        // 查询用户
        List<User> userList = userMapper.select(user);
        if (!CollectionUtils.isEmpty(userList)) {
            user = userList.get(0);
            // 生成token
            onLogin(user);
            return user;
        }

        throw new UserException(UserException.UserType.USER_AUTH_FAIL, "鉴权失败");
    }

    /**
     * 生成token
     * @param user
     */
    private void onLogin(User user) {
        // 利用jwt生成token
        String token = JwtHelper.genToken(ImmutableMap.of("name", user.getName(), "email", user.getEmail(), "timestamp", Instant.now().getEpochSecond() + ""));
        // 设置token
        String newToken = resetToken(token, user.getEmail());
        user.setToken(newToken);
    }

    /**
     * 鉴权
     * @param token
     * @return
     */
    @Override
    public User getLoginUserByToken(String token) {
        Map<String, String> map = null;
        try {
            // 通过jwt进行鉴权
          map = JwtHelper.verifyToken(token);
        }catch (Exception e) {
            throw new UserException(UserException.UserType.USER_NOT_LOGIN, "Token失效");
        }
        // 获取邮件
        String email = map.get("email");
        // 验证邮件是否失效在redis中
        Long expire = redisTemplate.getExpire(email);
        // 如果没有失效，就email的过期时间
        if (expire > 0L) {
            String newToken = resetToken(token, email);
            User user = getUesrEmail(email);
            user.setToken(newToken);
            // 获取用户
            return user;
        }

        throw new UserException(UserException.UserType.USER_NOT_LOGIN, "Token失效，请重新登录");
    }

    @Override
    public void logout(String token) {
        Map<String, String> map = JwtHelper.verifyToken(token);
        redisTemplate.delete(map.get("email"));
    }

    @Async
    protected void registerNotify(String email, String enabelUrl) {
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

    /**
     * 重置token的过期时间
     * @param token
     * @param email
     * @return
     */
    private String resetToken(String token, String email) {
        // 设置token
        redisTemplate.opsForValue().set(email, token);
        // 设置过期时间
        redisTemplate.expire(email, 30, TimeUnit.MINUTES);

        return token;
    }

    /**
     * 根据邮箱获取用户
     * @param email
     * @return
     */
    private User getUesrEmail(String email) {
        User user = new User();
        user.setEmail(email);
        List<User> userList = userMapper.select(user);
        if (!CollectionUtils.isEmpty(userList)) {
            return userList.get(0);
        }
        // 用户不存在异常
        throw new UserException(UserException.UserType.USER_NOT_FOUND, "用户不存在");
    }

}
