package com.ruanmou.house.user.service.impl;

import com.google.gson.Gson;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.ruanmou.house.user.common.MyPutRet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @ProjectName: house
 * @Package: com.ruanmou.house.user.service.impl
 * @ClassName: QiNiuService
 * @Author: majiafei
 * @Description: 七牛云上传
 * @Date: 2018/12/19 21:47
 */
@Service
public class QiNiuService {

    @Autowired
    private UploadManager uploadManager;

    @Autowired
    private Auth auth;

    @Value("${qiniu.bucket}")
    private String bucket;

    @Value("${qiniu.urlPrefix}")
    private String urlPrefix;

    public String uploadFile(MultipartFile file) {
        try {
            // 上传文件
            Response response = uploadManager.put(file.getBytes(), null, auth.uploadToken(bucket));
            // 将返回的结果解析成对象
            MyPutRet myPutRet = new Gson().fromJson(response.bodyString(), MyPutRet.class);

            // 将路径返回
            return urlPrefix + myPutRet.getKey();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
