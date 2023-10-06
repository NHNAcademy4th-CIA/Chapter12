package org.nhnacademy.lsj;

public class Example {
}


class NamedThread extends Thread {
    private String name;  // The name of this thread.

    public NamedThread(String name) {  // Constructor gives name to thread.
        this.name = name;
    }

    public void run() {  // The run method prints a message to standard output.
        System.out.println("Greetings from thread '" + name + "'!");
    }
}

class NamedRunnable implements Runnable {
    private String name;  // The name of this Runnable.

    public NamedRunnable(String name) {  // Constructor gives name to object.
        this.name = name;
    }


    @Override
    public void run() {
        System.out.println("Greetings from runnable '" + name + "'!");
    }
}
