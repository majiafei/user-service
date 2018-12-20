package com.ruanmou.house;

import java.util.*;
import java.util.concurrent.*;

/**
 * @ProjectName: house
 * @Package: com.ruanmou.house
 * @ClassName: Mater
 * @Author: majiafei
 * @Description:
 * @Date: 2018/12/20 8:58
 */
public class Mater {

    volatile int count = 0;

    private HashMap<String, String> hashMap = new HashMap<>();


    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        CountDownLatch countDownLatch = new CountDownLatch(3);
        Worker worker1 = new Worker(countDownLatch, "张三");
        Worker worker2 = new Worker(countDownLatch, "李四");
        Worker worker3 = new Worker(countDownLatch, ":王五");

        Boss boss = new Boss(countDownLatch);

        executorService.execute(worker3);
        executorService.execute(boss);
        executorService.execute(worker2);
        executorService.execute(worker1);
        executorService.shutdown();
    }


    private static void pool() {
        int count = 200000;
        // 开始时间
        long startTime = System.currentTimeMillis();
        // 定义一个集合
        List<Integer> list = new LinkedList<>();
        // 定义一个线程池
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1, 60, TimeUnit.SECONDS, new LinkedBlockingDeque<>(count));
        Random random = new Random();

        for (int i = 0; i < count; i++) {
            threadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    list.add(random.nextInt());
                }
            });
        }
        threadPoolExecutor.shutdown();
        try {
            threadPoolExecutor.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(System.currentTimeMillis() - startTime);
        System.out.println(list.size());
    }

    private static void thread() {
        // 开始时间
        long startTime = System.currentTimeMillis();
        // 定义一个集合
        List<Integer> list = new LinkedList<>();
        Random random = new Random();

        for (int i = 0; i < 200000; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    list.add(random.nextInt());
                }
            });
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println(System.currentTimeMillis() - startTime);
        System.out.println(list.size());
    }

}
