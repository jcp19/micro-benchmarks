package rexbenchmarks;

import pt.minha.api.sim.Global;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Example1 implements IExample{
    public void mainMinha(String[] args){
        Lock lock = new ReentrantLock();
        Counter c = new Counter();
        Thread1E1 t1 = new Thread1E1(lock, c);
        Thread2E1 t2 = new Thread2E1(lock, c);
        t1.start();
        t2.start();
    }

    public static void main(String[] args) {
        Lock lock = new ReentrantLock();
        Counter c = new Counter();
        Thread1E1 t1 = new Thread1E1(lock, c);
        Thread2E1 t2 = new Thread2E1(lock, c);
        t1.start();
        t2.start();
    }
}




