package rexbenchmarks;

import pt.minha.api.sim.Global;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Global
public class Example1 {
    public static void main(String[] args) {
        Lock lock = new ReentrantLock();
        Counter c = new Counter();
        Thread1E1 t1 = new Thread1E1(lock, c);
        Thread2E1 t2 = new Thread2E1(lock, c);
        t1.start();
        t2.start();
    }
}


class Thread1E1 extends Thread {
    Lock lock;
    Counter c;

    Thread1E1(Lock lock, Counter c) {
        this.lock = lock;
        this.c = c;
    }
    @Override
    public void run() {
        for(int i=0; i<10; i++) {
            lock.lock();
            c.x = 2;
            lock.unlock();
        }

    }
}

class Thread2E1 extends Thread {
    Lock lock;
    Counter c;

    Thread2E1(Lock lock, Counter c) {
        this.lock = lock;
        this.c = c;
    }
    @Override
    public void run() {
        for(int i=0; i<10; i++) {
            lock.lock();
            c.x = 1;
            lock.unlock();
        }
    }
}

