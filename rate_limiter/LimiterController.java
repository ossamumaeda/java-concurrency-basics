package rate_limiter;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;

public class LimiterController {

    private Queue<Long> limiter = new LinkedList<>();

    public synchronized Long check(BlockingQueue<Integer> tasks, int i) {

        int max_requests = 10;

        // Remover os itens de queue com mais de 1 segundo
        Long now = System.nanoTime();
        while (!limiter.isEmpty() && now - limiter.peek() > 1_000_000_000L) { // Enquanto o elemento atual da limiter ja tiver
            // passado mais de 1 segundo retira
            limiter.poll();
        }

        if (limiter.size() < max_requests) { // Tem espaço
            System.out.println("Tem espaço! " + now);
            limiter.add(now);
            tasks.add(i);
            return -1L;

        } else {
            System.out.println("Sem espaço! " + now);
            return (1_000_000_000L + limiter.peek()) - now;
        }

    }

}
