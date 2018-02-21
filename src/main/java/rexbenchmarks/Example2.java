package rexbenchmarks;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Example2 {
      public static void main(String[] args) {
        Lock conditionOwner = new ReentrantLock();
        Condition canAdvance = conditionOwner.newCondition();
        Counter c = new Counter();

        Example2Thread1 t1 = new Example2Thread1(new ReentrantLock(), c, conditionOwner, canAdvance);
        Example2Thread2 t2 = new Example2Thread2(new ReentrantLock(), c, conditionOwner, canAdvance);
        t2.start();
        t1.start();
    }
}



