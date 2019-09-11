package MultiThread.ThreadCommunication.Problems;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/* 自定义锁，使用volatile和Unsafe类的CAS
 * 参考博客：https://www.cnblogs.com/tong-yuan/p/mylock.html
 * 存在问题
 */
public class LockCAS {
    private static int count = 0;

    public static void main(String[] args) throws InterruptedException {
        Lock lock = new Lock();
        CountDownLatch countDownLatch = new CountDownLatch(20);
        for (int i = 0; i < 20; ++i)
            new Thread(() -> {
                lock.lock();
                try {
                    System.out.println(Thread.currentThread().getName() + "上锁");
                    //TimeUnit.MILLISECONDS.sleep(50);
                    for (int j = 0; j < 1000; ++j) count++;
                }
                catch (Exception e) { e.printStackTrace(); }
                finally {
                    lock.unlock();
                    System.out.println(Thread.currentThread().getName() + "解锁");
                }
                countDownLatch.countDown();
            }, "tt-" + i).start();
        countDownLatch.await();
        System.out.println(count);
    }
}

class Lock {
    // 锁标记
    private volatile int state = 0;
    // 使用Unsafe的CAS原子操作
    private static Unsafe unsafe;
    // 获取内存偏移值，CAS方法中要用
    private static final long stateOffset;

    // CAS更新锁标记
    private boolean compareAndSetState(int expect, int update) {
        return unsafe.compareAndSwapInt(this, stateOffset, expect, update);
    }

    // 缓冲队列
    private static class Node {
        Thread thread;
        Node prev;
        Node next;
        public Node() { }
        public Node(Thread thread, Node prev) {
            this.thread = thread;
            this.prev = prev;
        }
    }

    private volatile Node head = null;
    private volatile Node tail = null;
    private final static long headOffset;
    private final static long tailOffset;

    private boolean compareAndSetHead(Node update) {
        return unsafe.compareAndSwapObject(this, headOffset, null, update);
    }

    private boolean compareAndSetTail(Node expect, Node update) {
        return unsafe.compareAndSwapObject(this, tailOffset, expect, update);
    }

    /* 入队
     * 仿照AQS中的入队操作
     */
    private Node enqueue() {
        for (; ;) {
            Node t = tail;
            if (t == null) {
                // head是个空结点
                if (compareAndSetHead(new Node()))
                    tail = head;
            } else {
                Node node = new Node(Thread.currentThread(), t);
                if (compareAndSetTail(t, node)) {
                    t.next = node;
                    return node;
                }
            }
        }
    }

    public void lock() {
        if (compareAndSetState(0, 1)) {
            return;
        }
        Node node = enqueue();
        Node prev = node.prev;
        while (node.prev != head || !compareAndSetState(0, 1)) {
            unsafe.park(false, 0L);
        }
        // 清空node,设置为新head
        head = node;
        node.thread = null;
        node.prev = null;
        // 回收原head
        prev.next = null;
    }

    public void unlock() {
        state = 0;
        Node next = head.next;
        if (next != null) {
            unsafe.unpark(next.thread);
        }
    }

    static {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            unsafe = (Unsafe) f.get(null);
            stateOffset = unsafe.objectFieldOffset(Lock.class.getDeclaredField("state"));
            headOffset = unsafe.objectFieldOffset(Lock.class.getDeclaredField("head"));
            tailOffset = unsafe.objectFieldOffset(Lock.class.getDeclaredField("tail"));
        } catch (Exception e) {
            throw new Error(e);
        }
    }
}