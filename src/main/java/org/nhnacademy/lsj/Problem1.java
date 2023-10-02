package org.nhnacademy.lsj;

import java.util.InputMismatchException;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Problem1 {

    private static final Logger logger = LoggerFactory.getLogger(Problem1.class);
    private static final Scanner sc = new Scanner(System.in);

    private static class Counter {
        private int count;

        private int countIncreaseUnit;

        void inc() {
            count = count + 1;
        }

        int getCount() {
            return count;
        }

        void setCountIncreaseUnit(int number) {
            countIncreaseUnit = number;
        }

        int getCountIncreaseUnit() {
            return countIncreaseUnit;
        }


    }

    private static final Counter counter = new Counter();

    private static class CountThread extends Thread {

        public void run() {  // The run method prints a message to standard output.
            for (int i = 0; i < counter.getCountIncreaseUnit(); i++) {
                counter.inc();
            }
            logger.info("{}", counter.getCount());
        }
    }


    public static void protlbm1() {


        logger.info("사용할 스레드 수를 입력해 주세요");

        CountThread[] countThreads = new CountThread[getNumber()];

        logger.info("카운터를 증가시킬 횟수를 입력해 주세요");

        counter.setCountIncreaseUnit(getNumber());

        logger.info("동기화를 하려면 Y 아니면 아무 키나 입력해 주세요");

        sc.nextLine();

        if (sc.nextLine().equals("Y")) {
            synchronizedCount(countThreads);
        } else {
            nonSynchronizedCount(countThreads);
        }

    }

    public static int getNumber() {

        int num = 0;

        try {
            num = sc.nextInt();
        } catch (InputMismatchException e) {
            logger.warn("입력이 정수가 아닙니다");
            getNumber();
        }
        return num;
    }

    public static void synchronizedCount(CountThread[] countThreads) {


        for (int i = 0; i < countThreads.length; i++) {
            countThreads[i] = new CountThread();
            countThreads[i].start();
            try {
                countThreads[i].join();
            } catch (InterruptedException e) {
                logger.warn("에러발생");
            }
        }
    }

    public static void nonSynchronizedCount(CountThread[] countThreads) {

        for (int i = 0; i < countThreads.length; i++) {
            countThreads[i] = new CountThread();
            countThreads[i].start();

        }
    }


}






