package rexbenchmarks;

import java.util.concurrent.locks.ReentrantLock;


public class Example1 implements IExample{
    public void mainMinha(String[] args){
        Counter c = new Counter();
        Example1Thread1 t1 = new Example1Thread1(new ReentrantLock(), c);
        Example1Thread2 t2 = new Example1Thread2(new ReentrantLock(), c);
        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        Counter c = new Counter();
        Example1Thread1 t1 = new Example1Thread1(new ReentrantLock(), c);
        Example1Thread2 t2 = new Example1Thread2(new ReentrantLock(), c);
        t1.start();
        t2.start();
    }
}




