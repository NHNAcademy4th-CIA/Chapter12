package org.nhnacademy.leejungbum;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Quiz1 {
    private static Logger logger = LoggerFactory.getLogger(Quiz1.class);

    public Quiz1() {
        int numberOfThreads = 100;
        int numberOfIncrements = 100;
        customThread[] workers = new customThread[numberOfThreads];
        Counter counter = new Counter();
        for (int i = 0; i < numberOfThreads; i++)
            workers[i] = new customThread();
        for (int i = 0; i < numberOfThreads; i++)
            workers[i].start();

        for (int i = 0; i < numberOfThreads; i++) {
            try {
                workers[i].join();
            } catch (InterruptedException e) {
            }
        }

        logger.info("예상 값 : {}", numberOfIncrements * numberOfThreads);
        logger.info("실제 값: {}", counter.getCount());

    }

}

class customThread extends Thread {
    public void run() {
        for (int i = 0; i < 100; i++) {
            Counter.inc();
        }
    }
}

class Counter {
    private static int count;

    public static void inc() {
        count = count + 1;
    }

    public static int getCount() {
        return count;
    }
}