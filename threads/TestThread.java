

/*
Exemplo syncronized
*/

public class TestThread extends Thread {
    String name;
    TheDemo theDemo;

    public TestThread(String name, TheDemo theDemo) {
        this.theDemo = theDemo;
        this.name = name;
        start();
    }

    @Override
    public void run() {
        theDemo.test(name);
    }

    public static class TheDemo {
        public synchronized void test(String name) {
            for (int i = 0; i < 10; i++) {
                System.out.println(name + " :: " + i);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public static void main(String[] args) {
        TheDemo theDemo = new TheDemo();
        @SuppressWarnings("unused")
        TestThread t = new TestThread("THREAD 1", theDemo);
        @SuppressWarnings("unused")

        TestThread t2 = new TestThread("THREAD 2", theDemo);
        @SuppressWarnings("unused")

        TestThread t3 = new TestThread("THREAD 3", theDemo);
    }
}
   
