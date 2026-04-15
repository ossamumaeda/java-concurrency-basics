package rate_limiter;

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
        BlockingQueue<Long> queue = new LinkedBlockingDeque<>();

        BlockingQueue<Integer> tasksQueue = new ArrayBlockingQueue<>(5);

        // PRODUTOR
        new Thread(() -> {
            int i = 0;
            while (true) {
                try {

                    // Dar um jeito de usar a queue aqui e validar se pode colocar uma nova task?

                    System.out.println("Produzindo: " + i);
                    tasksQueue.put(i++); // bloqueia se cheio
                    Thread.sleep(500);
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

    public static long verifyQueue(BlockingQueue<Long> queue){
        
        int max_requests = 10;
        try {

            // Remover os itens de queue com mais de 1 segundo
            Long now = System.nanoTime();
            while (!queue.isEmpty() && now - queue.peek() > 1_000_000_000L) { // Enquanto o elemento atual da queue ja tiver passado mais de 1 segundo retira
                queue.take();
            }

            if (queue.size() < max_requests) { // Tem espaço
                System.out.println("Tem espaço! " + now);
                return -1L;

            } else {
                System.out.println("Sem espaço! " + now);
                return (1_000_000_000L + queue.peek()) - now;
            }


        } catch (InterruptedException e) {
            return -1L;
        }

    }

}
