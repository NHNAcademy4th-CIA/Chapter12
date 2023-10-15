package org.nhnacademy.leejungbum;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * 과연 쓰레드 실행시 원하는대로 결과가 나올것인가?
 */
public class Quiz1 {
    private static Logger logger = LoggerFactory.getLogger(Quiz1.class);
    private static final int numberOfThreads = 100;
    private static final int numberOfIncrements = 100;
    public Quiz1() {

        customThread[] workers = new customThread[numberOfThreads];
        Counter counter = new Counter();
        for (int i = 0; i < numberOfThreads; i++)
            workers[i] = new customThread();
        for (int i = 0; i < numberOfThreads; i++)
            workers[i].start();

        for (int i = 0; i < numberOfThreads; i++) {
            try {
                workers[i].join();
            } catch (InterruptedException e) {
            }
        }

        logger.info("예상 값 : {}", numberOfIncrements * numberOfThreads);
        logger.info("실제 값: {}", counter.getCount());

    }

}

/***
 * 사용자 정의 쓰레드. 100번반복
 */
class customThread extends Thread {
    public void run() {
        for (int i = 0; i < 100; i++) {
            Counter.inc();
        }
    }
}

/***
 * 더하기
 */
class Counter {
    private static int count;

    public static void inc() {
        count = count + 1;
    }

    public static int getCount() {
        return count;
    }


}