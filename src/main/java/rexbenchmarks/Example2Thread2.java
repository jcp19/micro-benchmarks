package rexbenchmarks;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

class Example2Thread2 extends Thread {
    Lock lock;
    Counter c;
    Lock conditionOwner;
    Condition canAdvance;

    Example2Thread2(Lock lock, Counter c, Lock conditionOwner, Condition canAdvance) {
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
                if(c.x < 10) {
                    //System.out.println("\t\tT2: await");
                    canAdvance.await();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conditionOwner.unlock();
            }
            c.x = 10+i;
            //System.out.println("\t\tT2: c.x = "+c.x);
            lock.unlock();
        }
    }
}
