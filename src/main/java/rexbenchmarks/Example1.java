package rexbenchmarks;

import pt.minha.api.sim.Global;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Example1 implements IExample{
    public void mainMinha(String[] args){
        Counter c = new Counter();
        Thread1E1 t1 = new Thread1E1(new ReentrantLock(), c);
        Thread2E1 t2 = new Thread2E1(new ReentrantLock(), c);
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
        Thread1E1 t1 = new Thread1E1(new ReentrantLock(), c);
        Thread2E1 t2 = new Thread2E1(new ReentrantLock(), c);
        t1.start();
        t2.start();
    }
}




