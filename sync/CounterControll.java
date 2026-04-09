package sync;
public class CounterControll {

    private int counter = 0;

    public synchronized void add(int n) {
        int temp = this.counter;

        // Artificial delay to force race condition
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
        }

        this.counter = temp + n;
    }

    public int get() {
        return this.counter;
    }

}
