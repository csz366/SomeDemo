package MultiThread.ThreadCommunication.ProducerAndConsumer;

public class ProducerConsumerDemo2 {
    public static void main(String[] args) {
        Resource2 r = new Resource2();
        Producer2 pro = new Producer2(r);
        Consumer2 con = new Consumer2(r);
        Thread t1 = new Thread(pro);
        Thread t2 = new Thread(con);
        Thread t3 = new Thread(pro);
        Thread t4 = new Thread(con);
        t1.start();
        t2.start();
        t3.start();
        t4.start();
    }
}

class Resource2 {
    private String name;
    private int count = 1;
    private boolean flag = false;

    public synchronized void set(String name) {
        while (flag)
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        this.name = name + "---" + count++;
        System.out.println(Thread.currentThread().getName() + "...生产者..." + this.name);
        flag = true;
        this.notifyAll();
    }

    public synchronized void out() {
        while (!flag)
            try{ wait(); } catch (Exception e) {}
        System.out.println(Thread.currentThread().getName() + "...消费者..." + this.name);
        flag = false;
        this.notifyAll();
    }
}

class Producer2 implements Runnable {
    private Resource2 res;

    Producer2(Resource2 res) {
        this.res = res;
    }

    @Override
    public void run() {
        while (true) {
            res.set("商品");
        }
    }
}

class Consumer2 implements Runnable {
    private Resource2 res;
    Consumer2(Resource2 res) { this.res = res; }

    @Override
    public void run() {
        while (true) res.out();
    }
}