package org.nhnacademy.minju;

import java.util.InputMismatchException;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * .비동기 카운터 클래스
 * 실제 카운트와 이론상 카운트를 비교
 */
public class Exercise1 {
    private static final Logger logger = LoggerFactory.getLogger(Exercise1.class);

    /**
     * Counter 객체에서 inc()를 반복적으로 호출하는 스레드 클래스
     */
    private static class Counter {
        private int count;

        private void inc() {
            count = count + 1;
        }

    }

    private static class IncrementThread extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < increment; i++) {
                counter.inc();
            }
        }
    }

    // 객체는 공유 전역 변수
    private static Counter counter;
    private static int increment;

    /**
     * 사용자가 스레드 수와 각 스레드가 카운터를 증가시키는 횟수를 입력
     */
    public static void exercise1() {
        Scanner scanner = new Scanner(System.in);

        try {
            logger.info("스레드 수를 입력하세요.");
            int threadNumber = scanner.nextInt();
            if (threadNumber <= 0) { // NegativeArraySizeException
                throw new InputMismatchException("스레드의 수가 0보다 작습니다.");
            }

            // 각 스레드가 카운터를 증가시키는 횟수
            logger.info("thread increment : ");
            increment = scanner.nextInt();
            if (increment <= 0) {
                throw new InputMismatchException("카운터 증가 횟수는 1 이상이어야 합니다.");
            }

            IncrementThread[] threads = new IncrementThread[threadNumber];
            counter = new Counter();
            for (int i = 0; i < threadNumber; i++) {
                threads[i] = new IncrementThread();
                threads[i].start();
                try {
                    threads[i].join();
                } catch (InterruptedException e) {
                    logger.warn("{}", e.getMessage());
                }
            }

            logger.info("result : {}", increment * threadNumber);
            logger.info("count : {}", counter.count);
        } catch (InputMismatchException e) {
            logger.warn("{}", e.getMessage());
        }
    }
}
