package org.nhnacademy.leejungbum;

import java.util.ArrayList;
import java.util.concurrent.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * Callable 응용
 */
public class Quiz4 {
    public Quiz4() {
        new ThreadTest4();
    }
}

class ThreadTest4 {
    private static final Logger logger = LoggerFactory.getLogger(Quiz2.class);
    private static final int RANGE = 10000;
    private static class Result {

        private int divisorCount;
        private int divisorCountIndex;

        Result(int divisorCount, int divisorCountIndex) {
            this.divisorCount = divisorCount;
            this.divisorCountIndex = divisorCountIndex;
        }

        public int getDivisorCount() {
            return divisorCount;
        }

        public int getDivisorCountIndex() {
            return divisorCountIndex;
        }
    }

    /***
     * 쓰레드
     */
    private static class Task implements Callable<Result> {
        private int start;
        private int end;

        public Task(int start, int end) {
            this.start = start;
            this.end = end;
        }


        /***
         * divisor
         * @return 새로운 쓰레드
         */
        public Result call() {
            int maxDivisors = 0;
            int whichInt = 0;
            for (int i = start; i < end; i++) {
                int divisors = divisorSize(i);
                if (divisors > maxDivisors) {
                    maxDivisors = divisors;
                    whichInt = i;
                }
            }
            return new Result(maxDivisors,whichInt);
        }
    }

    /***
     * 약수 갯수
     * @param max 입력 값
     * @return 약수 갯수
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

    public ThreadTest4() {

        long start = System.currentTimeMillis();


        ExecutorService executor = Executors.newFixedThreadPool(10);

        ArrayList<Future<Result>> results = new ArrayList<>();


        for (int i = 0; i < 10; i++) {
            Task divisorTask = new Task(1 + (RANGE * i), RANGE* (i + 1));
            results.add(executor.submit(divisorTask));
        }

        executor.shutdown();


        int index = 0;
        int divisorCount = 0;

        for (var value : results) {
            try {
                Result result = value.get();
                index = result.getDivisorCountIndex();
                divisorCount = result.getDivisorCount();
            } catch (Exception e) {
                logger.warn("{}", e.getMessage());
            }
        }


        long elapsedTime = System.currentTimeMillis() - start;

        logger.info("Total elapsed time : {}  seconds", elapsedTime / 1000.0);
        logger.info("가장 제수 많은 정수 {}  이 정수의 제수 개수 {}", index, divisorCount);

    }

}