package rexbenchmarks;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Example3 implements IExample {
    public void mainMinha(String[] args) {
        Lock lock = new ReentrantLock();
        Counter c = new Counter();

        Thread1E3 t1 = new Thread1E3(lock, c);
        Thread2E3[] t2 = new Thread2E3[10];
        for(int i = 0; i < 10; i++) {
           t2[i] = new Thread2E3(lock, c, i+1);
        }
        t1.start();
        for(int i = 0; i < 10; i++) {
            t2[i].start();
        }
    }

public static void main(String[] args) {
        Lock lock = new ReentrantLock();
        Counter c = new Counter();

        Thread1E3 t1 = new Thread1E3(lock, c);
        Thread2E3[] t2 = new Thread2E3[10];
        for(int i = 0; i < 10; i++) {
           t2[i] = new Thread2E3(lock, c, i+1);
        }
        t1.start();
        for(int i = 0; i < 10; i++) {
            t2[i].start();
        }
    }
}

