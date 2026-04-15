package sync;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class syncCounter {
    public static void main(String[] args) {

        CounterControll c = new CounterControll();

        ExecutorService executorService = Executors.newCachedThreadPool();// 4-thread pool

        List<Callable<Void>> tasks = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            int taskId = i;
            tasks.add(() -> {
                System.out.println("Executing task: " + taskId);
                c.add(taskId);
                return null;
            });
        }

        try {
            executorService.invokeAll(tasks);
        } catch (InterruptedException e) {
        }
        executorService.shutdown();

        System.out.println(c.get());


    }

}
