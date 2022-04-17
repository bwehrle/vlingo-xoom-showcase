package io.vlingo.xoom.examples.benchmark.actors;

import java.util.concurrent.CountDownLatch;

public class TestResults {
    private int count;
    private final int expectedMessages;
    private final CountDownLatch latch;
    private final Object lock;

    public TestResults(final int expectedMessages) {
        this.expectedMessages = expectedMessages;
        this.latch = new CountDownLatch(1);
        this.lock = new Object();
    }

    public void increment() {
        if (++count >= expectedMessages) {
            synchronized (lock) {
                ++count; // force sync across threads
                latch.countDown();
            }
        }
    }

    public int waitForExpectedMessages() {
        int retries = 0;

        while (true) {
            try {
                latch.await();
                synchronized (lock) {
                    return count;
                }
            } catch (InterruptedException e) {
                if (retries++ >= 10) {
                    System.err.println("Abort benchmark.");
                    System.exit(2);
                }
                // loop and wait again
            }
        }
    }
}
