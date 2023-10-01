package org.nhnacademy.leejungbum;

public class Quiz1 {
    public Quiz1(){
        int numberOfThreads = 100;
        int numberOfIncrements = 100;
        customThread[] workers = new customThread[numberOfThreads];
        Counter counter = new Counter();
        for (int i = 0; i < numberOfThreads; i++)
            workers[i] = new customThread();
        for (int i = 0; i < numberOfThreads; i++)
            workers[i].start();

        for (int i = 0; i < numberOfThreads; i++) {
            try {
                workers[i].join();
            }
            catch (InterruptedException e) {
            }
        }

        System.out.println("예상 값 : "
                + (numberOfIncrements*numberOfThreads));
        System.out.println("실제 값: " + counter.getCount());


    }

}

class customThread extends Thread {
    public void run() {
        for (int i = 0; i < 100; i++) {
            Counter.inc();
        }
    }
}
class Counter{
    private static int count;

    public static void inc() {
        count = count+1;
    }
    public static int getCount() {
        return count;
    }
}