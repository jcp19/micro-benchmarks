package rexbenchmarks;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

class Thread2E2 extends Thread {
    Lock lock;
    Counter c;
    Lock conditionOwner;
    Condition canAdvance;

    Thread2E2(Lock lock, Counter c, Lock conditionOwner, Condition canAdvance) {
        this.lock = lock;
        this.c = c;
        this.conditionOwner = conditionOwner;
        this.canAdvance = canAdvance;
    }
    @Override
    public void run() {
        for(int i=0; i<10; i++) {
            lock.lock();
            conditionOwner.lock();
            // TODO: what about spurious wakes?
            try {
                canAdvance.await();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conditionOwner.unlock();
            }
            c.x = 1;
            lock.unlock();
        }
    }
}
