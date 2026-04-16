package rate_limiter;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

public class LimiterController {

    private Queue<Long> limiter = new LinkedList<>();
    private final ReentrantLock lock = new ReentrantLock();
    private int max_requests = 6;

    public ReturnDTO checkAndQueue(BlockingQueue<Integer> tasksQueue, int i) {

        lock.lock();
        try {

            Long now = System.nanoTime();
            while (!this.limiter.isEmpty() && now - this.limiter.peek() > 1_000_000_000L) {
                this.limiter.poll();
            }

            if (this.limiter.size() >= max_requests) {
                System.out.println("Sem espaço limiter:" + this.limiter.size());
                return new ReturnDTO(ValidQueueEnum.WAIT_LIMIT, (1_000_000_000L + limiter.peek()) - now);

            }

            if (tasksQueue.remainingCapacity() == 0) {
                System.out.println("Sem espaço Task " + now + "Limite:" + this.limiter.size());
                return new ReturnDTO(ValidQueueEnum.WAIT_QUEUE, 0L);
            }

            System.out.println("Tem espaço! " + now);
            tasksQueue.add(i);
            limiter.add(now);
            return new ReturnDTO(ValidQueueEnum.SUCCESS, 0L);

        } finally {
            lock.unlock();
        }

    }

}
