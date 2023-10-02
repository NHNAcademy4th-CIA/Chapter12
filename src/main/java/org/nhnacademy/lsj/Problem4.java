package org.nhnacademy.lsj;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Problem4 {

    private static final Logger logger = LoggerFactory.getLogger(Problem4.class);


    private static final int SIZE = 10;
    private static final int UNIT = 10000;


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

        public int getDivisorCount() {
            return divisorCount;
        }

        public int getNumber() {
            return number;
        }

        private int divisorCount;
        private int number;

        Result(int divisorCount, int number) {
            this.divisorCount = divisorCount;
            this.number = number;
        }
    }

    private static class DivisorTask implements Callable<Result> {
        private int min;
        private int max;

        public DivisorTask(int min, int max) {
            this.min = min;
            this.max = max;
        }

        public Result call() {

            int divisorCount = 0;
            int number = 0;

            for (int i = min; i <= max; i++) {

                int cnt = countDivisor(i);
                if (divisorCount < cnt) {
                    divisorCount = cnt;
                    number = i;
                }
            }
            return new Result(divisorCount, number);
        }
    }


    public static void main(String[] args) {

        long start = System.currentTimeMillis();


        ExecutorService executor = Executors.newFixedThreadPool(20);

        ArrayList<Future<Result>> results = new ArrayList<>();


        for (int i = 0; i < SIZE; i++) {
            DivisorTask divisorTask = new DivisorTask(1 + (UNIT * i), UNIT * (i + 1));
            results.add(executor.submit(divisorTask));
        }

        executor.shutdown();

        int number = 0;
        int divisorCount = 0;

        for (var value : results) {
            try {
                Result result = value.get();
                number = result.getNumber();
                divisorCount = result.getDivisorCount();
            } catch (Exception e) {
                logger.warn("{}", e.getMessage());
            }
        }



        long elapsedTime = System.currentTimeMillis() - start;

        logger.info("Total elapsed time : {}  seconds", elapsedTime / 1000.0);
        logger.info("가장 제수 많은 정수 {} \n 이 정수의 제수 개수 {}", number, divisorCount);

    }

}
