package rexbenchmarks;

import java.util.concurrent.locks.Lock;

public class Example3Thread1 extends Thread {
    Lock lock;
    Counter c;

    public Example3Thread1(Lock lock, Counter c) {
        this.lock = lock;
        this.c = c;
    }

    @Override
    public void run() {
        for(int i = 0; i < 10; i++) {
            //forces the read to happen
            System.out.println(c.x);
        }
        for(int i = 0; i < 10; i++) {
            lock.lock();
            c.x = 1;
            lock.unlock();
        }
        System.out.println("Thread 0 terminou");
    }
}
