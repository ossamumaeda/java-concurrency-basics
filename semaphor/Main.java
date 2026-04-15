package semaphor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/*
Ajuda a limitar acesso a recursos
Evita overload de processos
*/

public class Main {
    public static void main(String[] args) {

        ExecutorService ex = Executors.newFixedThreadPool(10);

        Semaphore semaphore = new Semaphore(5,true); 

        for (int i = 0; i < 100; i++) {
            int taskId = i;

            ex.submit(() -> {
                try {
                    System.out.println("Task " + taskId + " esperando...");
                    
                    semaphore.acquire(); // pega permissão

                    System.out.println("Task " + taskId + " entrou!");

                    // Thread.sleep(1000);

                } catch (InterruptedException e) {
                } finally {
                    semaphore.release(); // devolve permissão
                    System.out.println("Task " + taskId + " saiu!");
                }
            });
        }
    }
}
