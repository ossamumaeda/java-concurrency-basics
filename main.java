import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/*
Como usar ExecutorService com thread pool
*/

public class main {
    public static void main(String[] args) {
        List<Integer> l = new CopyOnWriteArrayList<>();
        ExecutorService executorService = Executors.newCachedThreadPool();// 4-thread pool

        List<Callable<Void>> tasks = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            int taskId = i;
            tasks.add(() -> {
                System.out.println("Executing task: " + taskId);
                l.add(taskId);
                return null;
            });
        }

        try {
            executorService.invokeAll(tasks);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } // blocks until ALL finish
        executorService.shutdown();

        System.out.println(l);

    }
}