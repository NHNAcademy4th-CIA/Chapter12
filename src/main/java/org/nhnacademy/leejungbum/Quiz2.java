package org.nhnacademy.leejungbum;

import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Quiz2 {
    public Quiz2(){
        new ThreadTest2();
    }
}

class ThreadTest2 {
    private static final Logger logger = LoggerFactory.getLogger(Quiz2.class);
    private static final int RANGE = 10000;
    private static int maxDivisors = 0;

    private static class CountDivisrThread extends Thread {
        private int start;
        private int end;
        public CountDivisrThread(int start, int end) {
            this.start = start;
            this.end = end;
        }

        public void run() {

            for (int i = start; i <= end; i++) {
                int cnt = divisorSize(i);
                if (maxDivisors < cnt) {
                    maxDivisors = cnt;
                }
                logger.info("{} : {}",i, cnt);
            }
        }
    }

    private static int divisorSize(int max) {
        int count = 0;
        for (int i = 1; i <= max; i++) {
            if (max % i == 0) {
                count++;
            }
        }
        return count;
    }
    public ThreadTest2() {

        long startTime = System.currentTimeMillis();

        CountDivisrThread[] countDivisrThreads = new CountDivisrThread[10];


        for (int i = 0; i < 10; i++) {
            countDivisrThreads[i] = new CountDivisrThread(1 + (RANGE * i), RANGE * (i + 1));
            countDivisrThreads[i].start();
        }

        for (int i = 0; i < 10; i++) {

            while (countDivisrThreads[i].isAlive()) {
                try {
                    countDivisrThreads[i].join();
                } catch (InterruptedException e) {
                    logger.warn("{}", e.getMessage());
                }
            }
        }
        long cTime = System.currentTimeMillis() - startTime;
        logger.info("약수를 가장 많이 가진 수 {}", maxDivisors);
        logger.info("총 걸린 시간 : {}", cTime / 1000.0);

    }


}