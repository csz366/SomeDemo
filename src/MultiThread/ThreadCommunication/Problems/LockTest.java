package MultiThread.ThreadCommunication.Problems;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/* 自定义锁
 * 使用了synchronized
 */

public class LockTest extends Thread {
    public static void main(String[] args) {
        final MyLock lock = new MyLock();
        for (int i = 1; i < 5; ++i) {
            new Thread(() -> {
                try {
                    TimeUnit.SECONDS.sleep(3);
                    lock.lock();
                    for (int i1 = 0; i1 < 5; ++i1)
                        System.out.println(i1);
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    lock.unlock();
                }
            }).start();
        }
    }
}

class MyLock {
    private boolean locked = false;
    private List<Thread> list = new LinkedList<>();
    private Thread lockBy;

    synchronized void lock() throws InterruptedException {
        while (locked && lockBy != Thread.currentThread()) {
            list.add(Thread.currentThread());
            wait();
        }
        list.remove(Thread.currentThread());
        lockBy = Thread.currentThread();
        locked = true;
        System.out.println(lockBy.getName() + "加锁");
    }

    synchronized void unlock() {
        if (Thread.currentThread() == lockBy) {
            locked = false;
            System.out.println(Thread.currentThread().getName() + "解锁");
            this.notifyAll();
            list.clear();
        }
    }
}