package MultiThread.CreateThread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class ThreadC implements Callable<String> {
    @Override
    public String call() throws Exception {
        return "wahaha";
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ThreadC c = new ThreadC();
        FutureTask<String> futureTask = new FutureTask<>(c);
        Thread t = new Thread(futureTask);
        t.start();
        System.out.println(futureTask.get());
    }
}
