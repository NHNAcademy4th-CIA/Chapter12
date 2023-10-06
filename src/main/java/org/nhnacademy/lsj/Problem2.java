package org.nhnacademy.lsj;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 1~100000 까지 약수를 가장 많이 갖고있는 정수와 , 약수의 개수를 출력.
 */
public class Problem2 {

    private static final int UNIT = 10000;

    private static final int SIZE = 10;

    private static final Logger logger = LoggerFactory.getLogger(Problem2.class);


    /**
     * 약수의 개수를 구하는 run 메서드 가진 thread.
     */
    private static class CountDivisrTask extends Thread {
        private int start;
        private int end;
        private int maxCount;
        private int id;

        public CountDivisrTask(int start, int end) {
            this.start = start;
            this.end = end;
            this.maxCount = 0;
        }

        public void run() {

            for(int i=0;i<100;i++){
                System.out.println(10);
            }

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
     * 약수의 개수 구함.
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

    /**
     * 임의로 작업을 나눴음 , thread 는 총 10개 , thread 하나가 10000개의 숫자 범위를 커버함.
     * 위에서 말한게 thread pool 을 의미함.
     * join 써서 access에 제한 검 , 주석처리한 부분은 single thread로 구한 것.
     * 체크해보니 multi thread 쓰는게 더 빠름.
     */
    public static void problem2() {

        long startTime = System.currentTimeMillis();

        CountDivisrTask[] countPrimesTasks = new CountDivisrTask[SIZE];


        for (int i = 0; i < SIZE; i++) {
            countPrimesTasks[i] = new CountDivisrTask(1 + (UNIT * i), UNIT * (i + 1));
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
//        for (int i = 1; i <= 100000; i++) {
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
