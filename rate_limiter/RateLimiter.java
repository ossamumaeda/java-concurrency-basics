package rate_limiter;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class RateLimiter {

    public static void main(String[] args) {

        // Limitar numero de requisições por segundo
        // Em vez de sempre liberar as threads disponives, como fazer o controle de qual pode ser usada a cada segundo?
        // Lista com as timestamps das ultimas requisições
        // Toda vez que uma nova requisição tentar ser executada:
        /* 
                1. Limpa as requests com mais de 1 segundo
                2. Conta quantas sobraram
                3. Verifica se pode adicionar
                4. Se não puder espere -> O tempo de espera é a diferença de tempo entre o timestamp mais antigo e o atual
         */
        LimiterController limiterController = new LimiterController();

        BlockingQueue<Integer> tasksQueue = new ArrayBlockingQueue<>(5);

        // PRODUTOR
        new Thread(() -> {
            int i = 0;
            while (true) {
                try {

                    // Dar um jeito de usar a queue aqui e validar se pode colocar uma nova task?
                    System.out.println("Produzindo: " + i);
                    Long ok = limiterController.check(tasksQueue, i++);
                    if(ok > 0){
                        Thread.sleep(ok);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // CONSUMIDOR
        new Thread(() -> {
            while (true) {
                try {
                    int value = tasksQueue.take(); // bloqueia se vazio
                    System.out.println("Consumindo: " + value);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

}
