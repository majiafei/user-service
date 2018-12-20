package com.ruanmou.house.user.common;

import lombok.Getter;
import lombok.Setter;

/**
 * @ProjectName: house
 * @Package: com.ruanmou.house.user.common
 * @ClassName: MyPutRet
 * @Author: majiafei
 * @Description:  上传文件后返回的属性
 * @Date: 2018/12/19 22:18
 */
@Getter
@Setter
public class MyPutRet {
    public String key;
    public String hash;
    public String bucket;
    public long fsize;

}
