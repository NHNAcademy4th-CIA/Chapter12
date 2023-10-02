package org.nhnacademy.lsj;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Problem2 {

    private static final int UNIT = 1000;

    private static final int SIZE = 10;

    private static final Logger logger = LoggerFactory.getLogger(Problem2.class);


    private static class CountPrimesTask extends Thread {
        private int start;
        private int end;
        private int maxCount;
        private int id;

        public CountPrimesTask(int start, int end) {
            this.start = start;
            this.end = end;
            this.maxCount = 0;
        }

        public void run() {

            for (int i = start; i <= end; i++) {

                int cnt = countDivisor(i); // 1부터 i까지의 약수 개수

                if (maxCount < cnt) {
                    maxCount = cnt;
                    id = i;
                }
            }
        }

        public int getMaxCount() {
            return maxCount;
        }

        public int getIdNumber() {
            return id;
        }
    }

    /**
     * Count the primes between min and max, inclusive.
     */
    private static int countDivisor(int max) {
        int count = 0;
        for (int i = 1; i <= max; i++) {
            if (max % i == 0) {
                count++;
            }
        }
        return count;
    }

    public static void problem2() {

        long startTime = System.currentTimeMillis();

        CountPrimesTask[] countPrimesTasks = new CountPrimesTask[SIZE];


        for (int i = 0; i < SIZE; i++) {
            countPrimesTasks[i] = new CountPrimesTask(1 + (UNIT * i), UNIT * (i + 1));
            countPrimesTasks[i].start();
        }

        for (int i = 0; i < SIZE; i++) {

            while (countPrimesTasks[i].isAlive()) {
                try {
                    countPrimesTasks[i].join();
                } catch (InterruptedException e) {
                    logger.warn("{}", e.getMessage());
                }
            }
        }


        int maxDivisorsCountNumber = 0;
        int divisorCount = 0;

        for (int i = 0; i < SIZE; i++) {

            int maxCount = countPrimesTasks[i].getMaxCount();

            if (maxCount > divisorCount) {
                divisorCount = maxCount;
                maxDivisorsCountNumber = countPrimesTasks[i].getIdNumber();
            }
        }

        long elapsedTime = System.currentTimeMillis() - startTime;
        logger.info("가장 제수 많은 정수 {} \n 이 정수의 제수 개수 {}", maxDivisorsCountNumber, divisorCount);
        logger.info("Total elapsed time : {}  seconds", elapsedTime / 1000.0);

//        long startTime2 = System.currentTimeMillis();
//
//        int maxCount = 0;
//        int id = 1;
//        for (int i = 1; i <= 10000; i++) {
//            int cnt = countDivisor(i); // 1부터 i까지의 약수 개수
//
//            if (maxCount < cnt) {
//                maxCount = cnt;
//                id = i;
//            }
//        }
//
//
//        long elapsedTime2 = System.currentTimeMillis() - startTime2;
//        logger.info("가장 제수 많은 정수 {} \n 이 정수의 제수 개수 {}", maxCount, id);
//        logger.info("Total elapsed time : {}  seconds", elapsedTime2 / 1000.0);


    }


}
