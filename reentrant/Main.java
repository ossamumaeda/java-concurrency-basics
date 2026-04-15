package reentrant;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    public static void main(String[] args) {
        RequestQueue queue = new RequestQueue();

        ExecutorService executor = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 100; i++) {
            int taskId = i;

            executor.submit(() -> {

                if (queue.tryProcess()) {
                    try {

                        System.out.println("Processing task " + taskId);                        
                        Thread.sleep(ThreadLocalRandom.current().nextInt(500, 1501));

                    } catch (InterruptedException e) {
                    } finally {
                        queue.finish();
                    }
                } else {
                    while (!queue.tryProcess()) {
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                        } // espera e tenta de novo
                    }
                }
            });
        }

        executor.shutdown();

    }
}
