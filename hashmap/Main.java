package hashmap;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {
        ConcurrentHashMap<String, Long> map = new ConcurrentHashMap<>();
        ExecutorService executor = Executors.newFixedThreadPool(10);

        for (int i = 0; i <= 100; i++) {
            executor.submit(() -> {

                map.merge("counter", 1L, Long::sum);

                // PUT seguro (não sobrescreve)
                map.putIfAbsent("initialized", 1L);
            });
        }


        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(map.get("counter")); 
    }
}
