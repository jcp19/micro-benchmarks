package rexbenchmarks;

import java.util.concurrent.locks.Lock;

public class Thread2E3 extends Thread {
    Lock lock;
    Counter c;
    int i;

    public Thread2E3(Lock lock, Counter c, int i) {
        this.lock = lock;
        this.c = c;
        this.i = i;
    }

    @Override
    public void run() {
        if(i <= 5) { lock.lock(); }
        c.x = 1;
        if(i <= 5) { lock.unlock(); }
    }
}
