package MultiThread.Management;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ThreadJoin implements Runnable {

    private String name;

    public ThreadJoin(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        System.out.printf("%s begin: %s\n", name, new Date());
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("%s has finished: %s\n", name, new Date());
    }

    public static void main(String[] args) {
        Thread thread1 = new Thread(new ThreadJoin("One"));
        Thread thread2 = new Thread(new ThreadJoin("two"));
        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Main thread is finished");
    }
}
