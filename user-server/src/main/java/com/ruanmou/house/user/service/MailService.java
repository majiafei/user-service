package com.ruanmou.house.user.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ProjectName: house
 * @Package: com.ruanmou.house.user.service
 * @ClassName: MailService
 * @Author: majiafei
 * @Description:
 * @Date: 2018/12/18 11:49
 */
@Service
public class MailService {

    @Resource
    private MailSender mailSender;

    @Value("${spring.mail.username}")
    private String sender;

    public void send(String subject, String content, String toMail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(subject);
        message.setText(content);
        message.setFrom(sender);
        message.setTo(toMail);
        // 发送邮件
        mailSender.send(message);
    }


}
