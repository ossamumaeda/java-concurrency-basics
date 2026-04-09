package reentrant;

import java.util.concurrent.locks.ReentrantLock;

public class RequestQueue {
    private int activeRequests = 0;
    private final int MAX = 3;

    private final ReentrantLock lock = new ReentrantLock();

    public boolean tryProcess() {
        if (!lock.tryLock()) return false;

        try {
            if (activeRequests >= MAX) return false;

            activeRequests++;
            return true;

        } finally {
            lock.unlock();
        }
    }

    public void finish() {
        lock.lock();
        try {
            activeRequests--;
        } finally {
            lock.unlock();
        }
    }
}