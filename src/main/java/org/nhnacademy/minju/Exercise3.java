package org.nhnacademy.minju;

import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.LinkedBlockingDeque;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * queue 2개를 사용
 * 1. 작업 대기열을 사용하여 스레드 풀에 의해 실행될 작업을 보관(task)
 * 2. 작업 결과를 결과 대기열에 배치(result)
 */
public class Exercise3 {
    private static ConcurrentLinkedDeque<Task> taskQueue;
    private static LinkedBlockingDeque<Result> resultQueue;
    private static final Logger logger = LoggerFactory.getLogger(Exercise3.class);
    private static final int RANGE = 10_0000;

    private static class Task implements Runnable {
        int maxDivisorNumber;
        int maxDivisor;
        int min;
        int max;

        public Task(int min, int max) {
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
            resultQueue.add(new Result(maxDivisor, maxDivisorNumber));
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

    private static class Result {
        private int maxDivisor;
        private int maxDivisorNumber;

        public Result(int maxDivisor, int maxDivisorNumber) {
            this.maxDivisor = maxDivisor;
            this.maxDivisorNumber = maxDivisorNumber;
        }

        public int getMaxDivisorNumber() {
            return maxDivisorNumber;
        }

        public int getMaxDivisor() {
            return maxDivisor;
        }
    }

    private static class CountDivisorThread extends Thread {
        @Override
        public void run() {
            while (true) {
                Task task = taskQueue.poll();
                if (task == null) {
                    break;
                }
                task.run();
            }
        }
    }

    private static void countDivisorWithThreads(int numberOfThreads) {
        logger.info("\nCounting divisor between {} and {} using {} threads",
                1, RANGE, numberOfThreads);
        long startTime = System.currentTimeMillis();
        resultQueue = new LinkedBlockingDeque<>();
        taskQueue = new ConcurrentLinkedDeque<>();
        CountDivisorThread[] worker = new CountDivisorThread[numberOfThreads];

        // 스레드
        for (int i = 0; i < numberOfThreads; i++) {
            worker[i] = new CountDivisorThread();
        }

        // 대기열에 작업을 추가
        for (int i = 0; i < RANGE; i++) {
            taskQueue.add(new Task(1 + RANGE * i, RANGE * (i + 1)));
        }

        for (int i = 0; i < numberOfThreads; i++) {
            worker[i].start();
        }

        int maxDivisorCount = 0;
        int divisorNumber = 0;
        // max값 구하기
        for (int i = 0; i < numberOfThreads; i++) {
            try {
                Result result = resultQueue.take();
                if (result.getMaxDivisor() > maxDivisorCount) {
                    maxDivisorCount = result.getMaxDivisor();
                    divisorNumber = result.getMaxDivisorNumber();
                }
            } catch (InterruptedException e) {
                logger.warn("{}", e.getMessage());
            }
        }
        long elapsedTime = System.currentTimeMillis() - startTime;
        logger.info("가장 제수가 많은 정수 : {}", maxDivisorCount);
        logger.info("제수의 개수 : {}", divisorNumber);
        logger.info("총 걸린 시간 : {}", elapsedTime);

    }

    public static void exercise3() {
        Scanner sc = new Scanner(System.in);
        int numberOfThreads;
        do {
            logger.info("스레드 개수를 입력하세요. 0 미만이면 종료됩니다.");
            numberOfThreads = sc.nextInt();
            countDivisorWithThreads(numberOfThreads);

        } while (numberOfThreads > 0);
    }
}
