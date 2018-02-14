package rexbenchmarks;
import java.util.concurrent.locks.Lock;

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

