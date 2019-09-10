package MultiThread.CreateThread;

public class ThreadA extends Thread {
    @Override
    public void run() {
        for (int i = 0; i < 5; ++i)
            System.out.println(i);
    }

    public static void main(String[] args) {
        ThreadA threadA = new ThreadA();
        threadA.start();
    }
}
