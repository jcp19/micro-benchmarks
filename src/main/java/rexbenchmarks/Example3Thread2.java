package rexbenchmarks;

import java.util.concurrent.locks.Lock;

public class Example3Thread2 extends Thread {
    Lock lock;
    Counter c;
    int i;

    public Example3Thread2(Lock lock, Counter c, int i) {
        this.lock = lock;
        this.c = c;
        this.i = i;
    }

    @Override
    public void run() {
        System.out.println("Start T"+i+((i <= 5) ? " - lock" : ""));
        if(i <= 5) { lock.lock(); }
        c.x = 1;
        if(i <= 5) { lock.unlock(); }
        System.out.println("End T"+i+((i <= 5) ? " - unlock" : ""));
    }
}
