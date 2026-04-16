package rate_limiter;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class RateLimiter {

    /*
    Rate limiter implementado com a ideia de sliding window
        -> Tentar controlar o fluxo de requisicoes baseado em "janelas" de tempo
        . A ideia do algoritmo eh sempre limpar atualizar/limpar a janela com base no tempo fixo
    */

    public static void main(String[] args) {

        LimiterController limiterController = new LimiterController();
        BlockingQueue<Integer> tasksQueue = new ArrayBlockingQueue<>(5);

        new Thread(() -> {

            int i = 0;
            while (true) {
                System.out.println("Produzindo: " + i);

                ReturnDTO retorno = limiterController.checkAndQueue(tasksQueue, i);

                if (retorno.status() == ValidQueueEnum.WAIT_LIMIT) {
                    
                    try {
                        Thread.sleep(retorno.time_await()/1000000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }

                } else if (retorno.status() == ValidQueueEnum.WAIT_QUEUE) {
                    
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }

                }  
                else if(retorno.status() == ValidQueueEnum.SUCCESS){
                    i++;
                }
            }

        }).start();

        new Thread(() -> {
            while (true) {
                try {
                    int value = tasksQueue.take(); // bloqueia se vazio
                    System.out.println("Consumindo: " + value);
                    // Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

}
