package MultiThread.Management;

import MultiThread.CreateThread.ThreadA;

public class ThreadSleep {
    public static void main(String[] args) throws InterruptedException {
        System.out.println(Thread.currentThread().getName());
        ThreadA threadA = new ThreadA();
        threadA.start();
        threadA.sleep(1000);
        Thread.sleep(10);
        for (int i = 0; i < 100; ++i)
            System.out.println("main " + i);
    }
}
