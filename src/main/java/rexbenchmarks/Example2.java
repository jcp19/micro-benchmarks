package rexbenchmarks;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Example2 {
    public static void main(String[] args) {
        Lock lock = new ReentrantLock();
        Lock conditionOwner = new ReentrantLock();
        Condition canAdvance = conditionOwner.newCondition();

        Counter c = new Counter();
        Thread1E2 t1 = new Thread1E2(lock, c, conditionOwner, canAdvance);
        Thread2E2 t2 = new Thread2E2(lock, c, conditionOwner, canAdvance);
        t1.start();
        t2.start();
    }
}



