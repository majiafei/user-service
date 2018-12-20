package com.ruanmou.house;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @ProjectName: house
 * @Package: com.ruanmou.house
 * @ClassName: Worker
 * @Author: majiafei
 * @Description:
 * @Date: 2018/12/20 9:57
 */
public class Worker implements Runnable {

    private CountDownLatch countDownLatch;
    private String name;

    public Worker(CountDownLatch countDownLatch, String name) {
        this.countDownLatch = countDownLatch;
        this.name = name;
    }

    @Override
    public void run() {
        System.out.println(name + "开始干活=======================");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(name + "活干完了");
        countDownLatch.countDown();
    }
}
