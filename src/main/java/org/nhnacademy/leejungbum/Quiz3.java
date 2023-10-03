package org.nhnacademy.leejungbum;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Quiz3 {
    public Quiz3() {
        new ThreadTest3();
    }
}

class ThreadTest3 {

    private static LinkedBlockingQueue<Result> resultQueue;

    private static ConcurrentLinkedQueue<Task> taskQueue;
    private static final Logger logger = LoggerFactory.getLogger(Quiz2.class);
    private static final int RANGE = 10000;
    private static int maxDivisors = 0;
    private static int maxDivisorsIndex = 0;
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
        private static class Task extends Thread {
            private int start;
            private int end;

            public Task(int start, int end) {
                this.start = start;
                this.end = end;
            }


            public void run() {

                for (int i = start; i <= end; i++) {
                    int cnt = divisorSize(i);
                    if (maxDivisors < cnt) {
                        maxDivisors = cnt;
                        maxDivisorsIndex = i;
                    }
                    resultQueue.add(new Result(maxDivisors, maxDivisorsIndex));
                    logger.info("{} : {}", i, cnt);
                }
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
        private static int divisorSize(int max) {
            int count = 0;
            for (int i = 1; i <= max; i++) {
                if (max % i == 0) {
                    count++;
                }
            }
            return count;
        }

        public ThreadTest3() {
            taskQueue = new ConcurrentLinkedQueue<>();
            resultQueue = new LinkedBlockingQueue<>();
            long startTime = System.currentTimeMillis();

            CountDivisorsThread[] countDivisorsThreads = new CountDivisorsThread[10];


            for (int i = 0; i < 10; i++) {
                countDivisorsThreads[i] = new CountDivisorsThread();
            }

            for (int i = 0; i < 10; i++) {
                taskQueue.add(new Task(1 + (RANGE * i), RANGE * (i + 1)));
            }

            for (int i = 0; i < countDivisorsThreads.length; i++) {
                countDivisorsThreads[i].start();
            }

            long cTime = System.currentTimeMillis() - startTime;
            logger.info("약수를 가장 많이 가진 수 {} : 약수 갯수 {}", maxDivisorsIndex, maxDivisors);
            logger.info("총 걸린 시간 : {}", cTime / 1000.0);

        }

}