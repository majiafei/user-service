package com.ruanmou.house;

import java.util.concurrent.CountDownLatch;

/**
 * @ProjectName: house
 * @Package: com.ruanmou.house
 * @ClassName: Boss
 * @Author: majiafei
 * @Description:
 * @Date: 2018/12/20 9:59
 */
public class Boss implements Runnable {

    private CountDownLatch countDownLatch;
    private String name = "老板";

    public Boss(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        System.out.println("等他们干完了再检查工作");
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("开始检查工作=======================");
    }
}
