package rexbenchmarks;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

class Example2Thread1 extends Thread {
    Lock lock;
    Lock conditionOwner;
    Condition canAdvance;
    Counter c;

    Example2Thread1(Lock lock, Counter c, Lock conditionOwner, Condition canAdvance) {
        this.lock = lock;
        this.c = c;
        this.conditionOwner = conditionOwner;
        this.canAdvance = canAdvance;
    }
    @Override
    public void run() {
        for(int i=0; i<10; i++) {
            System.out.println("1");
            lock.lock();
            c.x = i;
            System.out.println("T1: c.x = "+c.x);
            conditionOwner.lock();
            System.out.println("T1: signal");
            canAdvance.signalAll();
            conditionOwner.unlock();
            lock.unlock();
        }
    }
}

