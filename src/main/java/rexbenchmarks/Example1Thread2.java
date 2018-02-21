package rexbenchmarks;

import java.util.concurrent.locks.Lock;

class Example1Thread2 extends Thread {
    Lock lock;
    Counter c;

    Example1Thread2(Lock lock, Counter c) {
        this.lock = lock;
        this.c = c;
    }
    @Override
    public void run() {
        for(int i=0; i<10; i++) {
            lock.lock();
            // System.out.println("\t\tT2: c.x = 1 (iteration "+i+")");
            c.x = i;
            lock.unlock();
        }
    }
}

