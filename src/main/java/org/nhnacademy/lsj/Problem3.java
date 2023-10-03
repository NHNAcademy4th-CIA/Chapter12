package org.nhnacademy.lsj;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 이전 문제의 연장선 Queue 2개를 사용 , 작업을 저장하는 task queue , 결과를 저장하는 result queue.
 */
public class Problem3 {


    private static LinkedBlockingQueue<Result> resultQueue;

    private static ConcurrentLinkedQueue<Task> taskQueue;

    private static final Logger logger = LoggerFactory.getLogger(Problem3.class);


    private static final int SIZE = 10;


    private static final int UNIT = 10000;


    private static class Task implements Runnable {

        private int min;
        private int max;

        Task(int min, int max) {
            this.min = min;
            this.max = max;
        }

        @Override
        public void run() {
            int maxDivisors = 0;
            int whichInt = 0;
            for (int i = min; i < max; i++) {
                int divisors = countDivisor(i);
                if (divisors > maxDivisors) {
                    maxDivisors = divisors;
                    whichInt = i;
                }
            }
            resultQueue.add(new Result(maxDivisors, whichInt));
        }
    }

    private static int countDivisor(int max) {
        int count = 0;
        for (int i = 1; i <= max; i++) {
            if (max % i == 0) {
                count++;
            }
        }
        return count;
    }

    private static class Result {

        private int divisorCount;
        private int maxDivisorsCountNumber;

        Result(int divisorCount, int maxDivisorsCountNumber) {
            this.divisorCount = divisorCount;
            this.maxDivisorsCountNumber = maxDivisorsCountNumber;
        }

        public int getDivisorCount() {
            return divisorCount;
        }

        public int getMaxDivisorsCountNumber() {
            return maxDivisorsCountNumber;
        }
    }

    private static class CountDivisorsThread extends Thread {
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


    /**
     * thread는 10개 , problem2와 동일 , task queue에 task 10개 집어넣음 .
     * thread size 줄이면 실제로 느려짐 , parallel하지 못하게 되는거니까.
     * 여기서도 thread pool에 thread10개 있는거 .
     * thread start 되면 resulut queue에 결과 담김.
     */
    public static void problem3() {


        long startTime = System.currentTimeMillis();

        resultQueue = new LinkedBlockingQueue<>(); // 결과 큐
        taskQueue = new ConcurrentLinkedQueue<>(); // 작업 큐

        CountDivisorsThread[] workers = new CountDivisorsThread[SIZE];


        for (int i = 0; i < workers.length; i++) {
            workers[i] = new CountDivisorsThread(); // 실행시킬 쓰레드
        }

        for (int i = 0; i < SIZE; i++) {
            taskQueue.add(new Task(1 + (UNIT * i), UNIT * (i + 1)));
        }

        for (int i = 0; i < workers.length; i++) {
            workers[i].start();
        }


        int divisorCount = 0;
        int number = 0;


        for (int i = 0; i < SIZE; i++) {
            try {
                Result result = resultQueue.take();
                if (result.getMaxDivisorsCountNumber() > divisorCount) { // new maximum.
                    divisorCount = result.getMaxDivisorsCountNumber();
                    number = result.getDivisorCount();
                }
            } catch (InterruptedException e) {
                logger.warn("{}", e.getMessage());
            }
        }

        long elapsedTime = System.currentTimeMillis() - startTime;
        logger.info("Total elapsed time : {}  seconds", elapsedTime / 1000.0);
        logger.info("가장 제수 많은 정수 {} \n 이 정수의 제수 개수 {}", divisorCount, number);


    }

}
