import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
Como usar ExecutorService com thread pool

Pool -> Ajuda a controlar o numero de threads sendo criada
    - Reutilizacao de threads
*/

public class Main {
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
        } // blocks until ALL finish
        executorService.shutdown();

        System.out.println(l);

    }
}