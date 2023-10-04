package org.nhnacademy.minju;

import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * .다중 스레드를 사용하여 1부터 10000까지의 범위에서 제수가 가장 많은 정수를 찾는다
 * 프로그램이 끝나면 경과 시간, 제수 개수가 가장 많은 정수, 제수 개수를 출력
 */
public class Exercise2 {
    private static final Logger logger = LoggerFactory.getLogger(Exercise2.class);
    private static final int START = 10_0000;

    /**
     * run에서 제수가 가장 많은 정수를 찾는다
     */
    private static class CountDivisorThread extends Thread {
        int maxDivisorNumber;
        int maxDivisor;
        int min;
        int max;

        public CountDivisorThread(int min, int max) {
            this.min = min;
            this.max = max;
            this.maxDivisor = 0;
        }

        @Override
        public void run() {
            for (int i = min; i <= max; i++) {
                int divisor = countDivisor(i);
                if (divisor > maxDivisor) {
                    maxDivisor = divisor;
                    maxDivisorNumber = i;
                }
            }
        }

        public int getMaxDivisor() {
            return maxDivisor;
        }

        public int getMaxDivisorNumber() {
            return maxDivisorNumber;
        }

        /**
         * 제수를 센다
         *
         * @param number number
         * @return count
         */
        private static int countDivisor(int number) {
            int count = 0;
            for (int i = 1; i <= number; i++) {
                if (number % i == 0) {
                    count++;
                }
            }
            return count;
        }
    }

    /**
     * 처리할 작업을 스레드의 개수로 나누고 각 스레드가 각각의 작업을 처리하도록 한다.
     * join()을 이용해 스레드를 종료
     * @param numberOfThreads 스레드 개수
     */
    private static void countDivisorWithThreads(int numberOfThreads) {
        logger.info("\nCounting divisor between {} and {} using {} threads",
                1, START, numberOfThreads);
        long startTime = System.currentTimeMillis();
        CountDivisorThread[] worker = new CountDivisorThread[numberOfThreads];
        int perThread = START / numberOfThreads;

        for (int i = 0; i < numberOfThreads; i++) {
            worker[i] = new CountDivisorThread(1 + perThread * i, perThread * (i + 1));
            worker[i].start();
        }
        for (int i = 0; i < numberOfThreads; i++) {
            while (worker[i].isAlive()) {
                try {
                    worker[i].join();
                } catch (InterruptedException e) {
                    logger.warn("{}", e.getMessage());
                }
            }
        }

        int maxDivisorCount = 0;
        int divisorNumber = 0;
        // max값 구하기
        for (int i = 0; i < numberOfThreads; i++) {
            int maxDivisor = worker[i].getMaxDivisor();
            if (maxDivisor > divisorNumber) {
                divisorNumber = maxDivisor;
                maxDivisorCount = worker[i].getMaxDivisorNumber();
            }
        }
        long elapsedTime = System.currentTimeMillis() - startTime;
        logger.info("가장 제수가 많은 정수 : {}", maxDivisorCount);
        logger.info("제수의 개수 : {}", divisorNumber);
        logger.info("총 걸린 시간 : {}", elapsedTime);

    }

    public static void exercise2() {
        Scanner sc = new Scanner(System.in);
        int numberOfThreads;
        do {
            logger.info("스레드 개수 : ");
            numberOfThreads = sc.nextInt();
            countDivisorWithThreads(numberOfThreads);

        } while (numberOfThreads > 0);
    }

}
