package com.ruanmou.house.user.config;

import com.qiniu.common.Zone;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @ProjectName: house
 * @Package: com.ruanmou.house.user.config
 * @ClassName: QiNiuConfiguration
 * @Author: majiafei
 * @Description:
 * @Date: 2018/12/19 22:03
 */
@SpringBootApplication
public class QiNiuConfiguration {

    @Value("${qiniu.accessKey}")
    private String accessKey;

    @Value("${qiniu.secretKey}")
    private String secretKey;


    @Bean
    public Configuration configuration() {
        return new Configuration(Zone.zone2());
    }

    @Bean
    public UploadManager uploadManager(Configuration configuration) {
        UploadManager uploadManager = new UploadManager(configuration);
        return uploadManager;
    }

    @Bean
    public Auth auth() {
        return Auth.create(accessKey, secretKey);
    }

}
