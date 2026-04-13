package completabeFuture;

import java.util.concurrent.CompletableFuture;

/*
join() → quando precisa do resultado final
thenAccept() → quando quer executar algo (log, salvar, etc.)
thenApply() → quando quer transformar
thenCombine() → quando junta resultados
thenCompose() → quando encadeia async
*/

public class Main {
    public static void main(String[] args) {

        CompletableFuture<String> userFuture = CompletableFuture.supplyAsync(() -> getUser());

        CompletableFuture<String> ordersFuture = CompletableFuture.supplyAsync(() -> getOrders());

        CompletableFuture<String> recsFuture = CompletableFuture.supplyAsync(() -> getRecommendations());

        // Só para executar uma tarefa
        userFuture.thenAccept(result -> {
            System.out.println(result);
        });

        CompletableFuture<String> enriched = userFuture
                .thenCompose(r -> CompletableFuture.supplyAsync(() -> r + " [ENRICHED]"));
        
        // 🔥 Combinação final
        CompletableFuture<String> finalResult = userFuture
                .thenCombine(ordersFuture, (user, orders) -> user + " | " + orders)
                .thenCombine(recsFuture, (partial, recs) -> partial + " | " + recs)
                .thenCombine(enriched, (partial, recs) -> partial + " | " + recs);

        // 👇 Aqui você decide como finalizar
        String result = finalResult.join();

        System.out.println(result);
    }

    static String getUser() {
        sleep(500);
        return "User: Gabriel";
    }

    static String getOrders() {
        sleep(700);
        return "Orders: 10";
    }

    static String getRecommendations() {
        sleep(1500);
        return "Recs: 5 items";
    }

    static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception e) {
        }
    }
}
