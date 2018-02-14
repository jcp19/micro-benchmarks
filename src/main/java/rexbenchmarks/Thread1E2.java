package rexbenchmarks;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

class Thread1E2 extends Thread {
    Lock lock;
    Lock conditionOwner;
    Condition canAdvance;
    Counter c;

    Thread1E2(Lock lock, Counter c, Lock conditionOwner, Condition canAdvance) {
        this.lock = lock;
        this.c = c;
        this.conditionOwner = conditionOwner;
        this.canAdvance = canAdvance;
    }
    @Override
    public void run() {
        for(int i=0; i<10; i++) {
            lock.lock();
            c.x = 2;
            conditionOwner.lock();
            canAdvance.signalAll();
            conditionOwner.unlock();
            lock.unlock();
        }

    }
}

