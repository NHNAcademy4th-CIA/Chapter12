package org.nhnacademy.lsj;

import java.util.InputMismatchException;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 동기화 시키지 않은 프로그램이 오류가 나는지 체크하는 프로그램.
 */
public class Problem1 {

    private static final Logger logger = LoggerFactory.getLogger(Problem1.class);
    private static final Scanner sc = new Scanner(System.in);

    /**
     * nested class , count 로 숫자 샘.
     */
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

    /**
     * COunt 할 thread class .
     */
    private static class CountThread extends Thread {

        public void run() {  // The run method prints a message to standard output.
            for (int i = 0; i < counter.getCountIncreaseUnit(); i++) {
                counter.inc();
            }
            logger.info("{}", counter.getCount());
        }
    }


    /**
     * 사용할 스레드 수 입력 -> 카운터 증가시킬 횟수 입력 -> 동기화 여부 -> 실행.
     */
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

    /**
     * 숫자 입력받음 , 유효한지 체크.
     *
     * @return 입력받은 숫자 반환.
     */
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

    /**
     * join 으로 유사 동기화 시켰음 , 이걸로 count하면 차례대로 실행 됨.
     *
     * @param countThreads thread 배열 .
     */
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

    /**
     * 동기화없이 그냥 access함 , 정상적인 값 안나옴.
     *
     * @param countThreads thread 배열.
     */
    public static void nonSynchronizedCount(CountThread[] countThreads) {

        for (int i = 0; i < countThreads.length; i++) {
            countThreads[i] = new CountThread();
            countThreads[i].start();

        }
    }


}






