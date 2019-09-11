package MultiThread.ThreadCommunication.Problems;

import java.util.Random;

/* 题目要求，给出一个长度为10的数组，要求实现，三个人分别往里随机放入三种颜色的小球，二是另一个人取走某种颜色数量最多的小球
 * 用生产者消费者模式实现
 * 使用锁
 * 使用wait(), notify(), notifyAll()
 * 最终三种颜色可能的比例是什么
 */

public class BallTest {
    public static void main(String[] args) {
        Ball ball = new Ball();
        Runnable pro = new Prod(ball);
        Runnable con = new Cons(ball);
        Thread[] producer = new Thread[3];
        for (int i = 0; i < 3; ++i) producer[i] = new Thread(pro);
        Thread consumer = new Thread(con);
        for (int i = 0; i < 3; ++i) producer[i].start();
        consumer.start();
    }
}

class Ball {
    private final int MAX_SIZE = 10;
    private String[] cols = new String[] {"红色", "黄色", "蓝色"};
    private int[] total = new int[3];
    private int count = 0;


    public synchronized void insert(int color) {
        while (count == MAX_SIZE)
            try { wait(); } catch (Exception e) { System.out.println(e.getMessage()); }
        total[color]++;
        count++;
        System.out.printf("插入%s的小球\n", cols[color]);
        display();
        this.notifyAll();
    }

    public synchronized void remove() {
        while (count == 0)
            try { wait(); } catch (Exception e) { System.out.println(e.getMessage()); }
        int max = 0;
        for (int i = 0; i < 3; ++i) if (total[i] > total[max]) max = i;
        total[max]--;
        count--;
        System.out.printf("取走%s的小球\n", cols[max]);
        display();
        this.notifyAll();
    }

    private void display() {
        System.out.println("各球数量为：");
        for (int i = 0; i < 3; ++i) {
            System.out.printf("%s:%d%c", cols[i], total[i], (i < 2 ? ' ' : '\n'));
        }
    }
}

class Prod implements Runnable {
    private Ball ball;

    public Prod(Ball ball) {
        this.ball = ball;
    }

    @Override
    public void run() {
        Random random = new Random();
        while (true) ball.insert(random.nextInt(3));
    }
}

class Cons implements Runnable {
    private Ball ball;

    public Cons(Ball ball) {
        this.ball = ball;
    }

    @Override
    public void run() {
        while (true) ball.remove();
    }
}