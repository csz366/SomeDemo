package MultiThread.CreateThread;

public class ThreadR implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < 5; ++i)
            System.out.println(i);
    }

    public static void main(String[] args) {
        Runnable runnable = new ThreadR();
        Thread thread = new Thread(runnable);
        thread.start();
    }
}
