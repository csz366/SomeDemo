package MultiThread.ThreadCommunication.ProducerAndConsumer;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ProducerConsumerDemo4 {
    public static void main(String[] args) {
        BlockingQueue<String> b = new ArrayBlockingQueue<>(1);
        new Producer4(b).start();
        new Producer4(b).start();
        new Producer4(b).start();
        new Consumer4(b).start();
    }
}

class Producer4 extends Thread {
    private BlockingQueue<String> b;

    public Producer4(BlockingQueue<String> b) {
        this.b = b;
    }

    @Override
    public synchronized void run() {
        String[] str = new String[] {"java", "struts", "Spring"};
        for (int i = 0; i< 9999999; ++i) {
            System.out.println(getName() + "生产者准备生产集合元素！");
            try {
                b.put(str[i % 3]);
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(getName() + "生产完成：" + b);
        }
    }
}

class Consumer4 extends Thread {
    private BlockingQueue<String> b;

    public Consumer4(BlockingQueue<String> b) {
        this.b = b;
    }

    @Override
    public synchronized void run() {
        while (true) {
            System.out.println(getName() + "消费者准备消费集合元素！");
            try {
                sleep(1000);
                b.take();
            }
            catch (Exception e) {
                System.out.println(e);
            }
            System.out.println(getName() + "消费完：" + b);
        }
    }
}
