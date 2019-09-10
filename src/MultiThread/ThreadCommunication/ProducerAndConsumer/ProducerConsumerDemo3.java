package MultiThread.ThreadCommunication.ProducerAndConsumer;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumerDemo3 {
    public static void main(String[] args) {
        Resource3 r = new Resource3();
        Producer3 pro = new Producer3(r);
        Consumer3 con = new Consumer3(r);
        Thread t1 = new Thread(pro);
        Thread t2 = new Thread(con);
        Thread t3 = new Thread(pro);
        Thread t4 = new Thread(con);
        t1.start();
        t3.start();
        t2.start();
        t4.start();
    }
}

class Resource3 {
    private String name;
    private int count = 1;
    private boolean flag = false;
    private Lock lock = new ReentrantLock();
    private Condition condition_pro = lock.newCondition();
    private Condition condition_con = lock.newCondition();

    public void set(String name) {
        lock.lock();
        try {
            while (flag)
                condition_pro.await();
            this.name = name + "---" + count++;
            System.out.println(Thread.currentThread().getName() + "...生产者..." + this.name);
            flag = true;
            condition_con.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void out() {
        lock.lock();
        try {
            while (!flag)
                condition_con.await();
            System.out.println(Thread.currentThread().getName() + "...消费者..." + this.name);
            flag = false;
            condition_pro.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}

class Producer3 implements Runnable {
    private Resource3 res;

    Producer3(Resource3 res) {
        this.res = res;
    }

    @Override
    public void run() {
        while (true) {
            res.set("商品");
        }
    }
}

class Consumer3 implements Runnable {
    private Resource3 res;
    Consumer3(Resource3 res) { this.res = res; }

    @Override
    public void run() {
        while (true) res.out();
    }
}