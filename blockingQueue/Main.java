package blockingQueue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
        public static void main(String[] args) {
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(5);

        // PRODUTOR
        new Thread(() -> {
            int i = 0;
            while (true) {
                try {
                    System.out.println("Produzindo: " + i);
                    queue.put(i++); // bloqueia se cheio
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
                    int value = queue.take(); // bloqueia se vazio
                    System.out.println("Consumindo: " + value);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
