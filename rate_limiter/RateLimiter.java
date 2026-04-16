package rate_limiter;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class RateLimiter {

    public static void main(String[] args) {

        LimiterController limiterController = new LimiterController();
        BlockingQueue<Integer> tasksQueue = new ArrayBlockingQueue<>(5);

        new Thread(() -> {

            int i = 0;
            while (true) {
                System.out.println("Produzindo: " + i);

                ReturnDTO retorno = limiterController.checkAndQueue(tasksQueue, i);

                if (retorno.status().equals(ValidQueueEnum.WAIT_LIMIT)) {
                    
                    try {
                        Thread.sleep(retorno.time_await()/1000000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }

                } else if (retorno.status().equals(ValidQueueEnum.WAIT_QUEUE)) {
                    
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }

                }  
                else if(retorno.status().equals(ValidQueueEnum.SUCCESS)){
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
