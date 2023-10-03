package org.nhnacademy.leejungbum;

import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * 약수 쓰레드
 */
public class Quiz2 {
    public Quiz2() {
        new ThreadTest2();
    }
}

class ThreadTest2 {
    private static final Logger logger = LoggerFactory.getLogger(Quiz2.class);
    private static final int RANGE = 10000;
    private static int maxDivisors = 0;
    private static int maxDivisorsIndex = 0;

    /***
     * 약수구하는 쓰레드
     */
    private static class CountDivisrThread extends Thread {
        private int start;
        private int end;

        public CountDivisrThread(int start, int end) {
            this.start = start;
            this.end = end;
        }

        /***
         * 약수중 가장큰거
         */
        public void run() {

            for (int i = start; i <= end; i++) {
                int cnt = divisorSize(i);
                if (maxDivisors < cnt) {
                    maxDivisors = cnt;
                    maxDivisorsIndex = i;
                }
                logger.info("{} : {}", i, cnt);
            }
        }
    }

    /***
     * 약수 구하기
     * @param max
     * @return
     */
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
        logger.info("약수를 가장 많이 가진 수 {} : 약수 갯수 {}", maxDivisorsIndex, maxDivisors);
        logger.info("총 걸린 시간 : {}", cTime / 1000.0);

    }


}