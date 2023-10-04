package org.nhnacademy.minju;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * .ExecutorService, Futures, Callable 사용
 */
public class Exercise4 {
    private static final Logger logger = LoggerFactory.getLogger(Exercise4.class);
    private static final int START = 10_0000;

    // Callable 반환 타입
    private static class Result {
        private int divisor;
        private int divisorNumber;

        public Result(int divisor, int divisorNumber) {
            this.divisor = divisor;
            this.divisorNumber = divisorNumber;
        }

        public int getDivisor() {
            return divisor;
        }

        public int getDivisorNumber() {
            return divisorNumber;
        }
    }

    /**
     * Callable c 는 executor.submit(c) 를 호출 하여 ExecutorService 에 제출할 수 있습니다 .
     * 그런 다음 Callable 은 나중에 실행됩니다.
     * call 에서 maxDivisor
     */
    private static class CountDivisorTask implements Callable<Result> {
        int min;
        int max;

        public CountDivisorTask(int min, int max) {
            this.min = min;
            this.max = max;
        }

        public Result call() {
            int maxDivisor = 0;
            int maxDivisorNumber = 0;
            for (int i = min; i < max; i++) {
                int divisor = countDivisor(i);
                if (divisor > maxDivisor) {
                    maxDivisor = divisor;
                    maxDivisorNumber = i;
                }
            }
            return new Result(maxDivisor, maxDivisorNumber);
        }

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
     * Futures : 비동기 계산의 결과
     * executor에 submit  -> result를 Futures에 담는다 -> shutdown으로 스레드풀 종료
     * -> Futures에서 저장한 값을 꺼내온다
     * @param numberOfTasks number of tasks
     */
    private static void countDivisorWithExecutor(int numberOfTasks) {

        logger.info("Counting Divisors between {} and {} using {} tasks",
                1, START, numberOfTasks);
        long startTime = System.currentTimeMillis();

        // 스레드풀 생성, 프로세서당 스레드 1개
        int processors = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(processors);

        /* An ArrayList used to store the Futures that are created when the tasks
         * are submitted to the ExecutorService. */

        ArrayList<Future<Result>> results = new ArrayList<>();

        /* Create the subtasks, add them to the executor, and save the Futures. */

        int perThread = START / numberOfTasks;
        for (int i = 0; i < numberOfTasks; i++) {
            CountDivisorTask oneTask = new CountDivisorTask(1 + perThread * i, (i + 1) * perThread);
            Future<Result> oneResult = executor.submit(oneTask); // submit을 호출하여 ExecutorService에 제출
            results.add(oneResult);  // Save the Future representing the (future) result.
        }

        /* Executor has to be shut down, or its existence will stop the Java Virtual
         * Machine from exiting.  (Threads in the executor are not daemon threads.) */

        executor.shutdown();

        /* Add up the results from all of the subtasks.  Results are obtained from the
         * Futures by calling their get() methods.  The for loop will not complete
         * until all tasks have completed and returned their output. */

        int maxDivisorCount = 0;
        int divisorNumber = 0;
        for (Future<Result> res : results) {
            try {
                maxDivisorCount = res.get().getDivisor();
                divisorNumber = res.get().getDivisorNumber();
            } catch (Exception e) {
                logger.warn("Error occurred while computing: {}", e.getMessage());
            }
        }

        long elapsedTime = System.currentTimeMillis() - startTime;
        logger.info("가장 제수가 많은 정수 : {}", maxDivisorCount);
        logger.info("제수의 개수 : {}", divisorNumber);
        logger.info("총 걸린 시간 : {}", elapsedTime);
    }

    public static void exercise4() {
        int processors = Runtime.getRuntime().availableProcessors();
        if (processors == 1) {
            logger.warn("Your computer has only 1 available processor.");
            return;
        }
        countDivisorWithExecutor(processors);
    }
}
