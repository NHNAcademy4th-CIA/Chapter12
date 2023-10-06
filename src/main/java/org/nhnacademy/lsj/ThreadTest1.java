package org.nhnacademy.lsj;

import java.util.Scanner;

public class ThreadTest1 {

    private final static int MAX = 10_000_000;


    /**
     * When a thread belonging to this class is run it will count the
     * number of primes between 2 and MAX.  It will print the result
     * to standard output, along with its id number and the elapsed
     * time between the start and the end of the computation.
     */
    private static class CountPrimesThread extends Thread {
        int id;  // An id number for this thread; specified in the constructor.

        public CountPrimesThread(int id) {
            this.id = id;
        }

        public void run() {
            long startTime = System.currentTimeMillis();
            int count = countPrimes(2, MAX);
            long elapsedTime = System.currentTimeMillis() - startTime;
            System.out.println("Thread " + id + " counted " +
                    count + " primes in " + (elapsedTime / 1000.0) + " seconds.");
        }
    }


    /**
     * Start several CountPrimesThreads.  The number of threads, between 1 and 30,
     * is specified by the user.
     */
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int numberOfThreads = 0;
        while (numberOfThreads < 1 || numberOfThreads > 30) {
            System.out.print("How many threads do you want to use  (from 1 to 30) ?  ");
            numberOfThreads = sc.nextInt();
            if (numberOfThreads < 1 || numberOfThreads > 30) {
                System.out.println("Please enter a number between 1 and 30 !");
            }
        }
        System.out.println("\nCreating " + numberOfThreads + " prime-counting threads...");
        CountPrimesThread[] worker = new CountPrimesThread[numberOfThreads];

//        for (int i = 0; i < numberOfThreads; i++) {
//            worker[i] = new CountPrimesThread(i);
//        }
//
//        long startTime = System.currentTimeMillis();
//
//        for (int i = 0; i < numberOfThreads; i++) {
//            worker[i].start();
//        }
//        long elapsedTime = System.currentTimeMillis() - startTime;
//
//        System.out.println((elapsedTime / 1000.0)+" @!!!!!!");
//
//
//        System.out.println("Threads have been created and started.");



        long startTime = System.currentTimeMillis();
        for (int i = 0; i < numberOfThreads; i++) {
            worker[i] = new CountPrimesThread(i);
            worker[i].start();
        }
        for (int i = 0; i < numberOfThreads; i++) {
            try {
                worker[i].join();  // Wait until worker[i] finishes, if it hasn't already.
            }
            catch (InterruptedException e) {
            }
        }
// At this point, all the worker threads have terminated.
        long elapsedTime = System.currentTimeMillis() - startTime;
        System.out.println("Total elapsed time: " + (elapsedTime/1000.0) + " seconds");




    }


    /**
     * Compute and return the number of prime numbers in the range
     * min to max, inclusive.
     */
    private static int countPrimes(int min, int max) {
        int count = 0;
        for (int i = min; i <= max; i++) {
            if (isPrime(i)) {
                count++;
            }
        }
        return count;
    }


    /**
     * Test whether x is a prime number.
     * x is assumed to be greater than 1.
     */
    private static boolean isPrime(int x) {
        assert x > 1;
        int top = (int) Math.sqrt(x);
        for (int i = 2; i <= top; i++) {
            if (x % i == 0) {
                return false;
            }
        }
        return true;
    }


} // end class ThreadTest1
