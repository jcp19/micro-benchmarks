package rexbenchmarks;
import java.util.concurrent.locks.Lock;

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
            // System.out.println("T1: c.x = 2 (iteration "+i+")");
            c.x = 2;
            lock.unlock();
        }

    }
}

