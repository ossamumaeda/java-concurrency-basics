package completabeFuture;

import java.util.concurrent.*;
import java.util.function.Supplier;

public class Example {

    static ExecutorService executor = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {

        CompletableFuture<String> user = withRetry(() -> getUser(), 3)
                .orTimeout(1, TimeUnit.SECONDS)
                .exceptionally(ex -> "User: fallback");

        CompletableFuture<String> orders = withRetry(() -> getOrders(), 3)
                .orTimeout(1, TimeUnit.SECONDS)
                .exceptionally(ex -> "Orders: fallback");

        CompletableFuture<String> recs = withRetry(() -> getRecommendations(), 2)
                .orTimeout(800, TimeUnit.MILLISECONDS)
                .exceptionally(ex -> "Recs: fallback");

        CompletableFuture<String> result = user.thenCombine(orders, (u, o) -> u + " | " + o)
                .thenCombine(recs, (partial, r) -> partial + " | " + r);

        System.out.println(result.join());

        executor.shutdown();
    }

    // 🔁 Retry genérico
    public static <T> CompletableFuture<T> withRetry(Supplier<T> task, int retries) {
        return CompletableFuture.supplyAsync(task, executor)
                .handle((result, ex) -> {
                    if (ex == null) {
                        return CompletableFuture.completedFuture(result);
                    } else if (retries > 0) {
                        System.out.println("Retrying... remaining: " + retries);
                        return withRetry(task, retries - 1);
                    } else {
                        CompletableFuture<T> failed = new CompletableFuture<>();
                        failed.completeExceptionally(ex);
                        return failed;
                    }
                })
                .thenCompose(f -> f); // flatten
    }

    // 🔧 Simulações

    static String getUser() {
        sleep(300);
        return "User: Gabriel";
    }

    static String getOrders() {
        sleep(1200); // vai estourar timeout às vezes
        return "Orders: 10";
    }

    static String getRecommendations() {
        if (Math.random() < 0.5) {
            throw new RuntimeException("Random failure");
        }
        sleep(500);
        return "Recs: 5 items";
    }

    static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception e) {
        }
    }
}