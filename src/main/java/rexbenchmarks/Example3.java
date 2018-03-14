package rexbenchmarks;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Example3 {

public static void main(String[] args) {
        Lock lock = new ReentrantLock();
        Counter c = new Counter();

        Example3Thread1 t1 = new Example3Thread1(lock, c);
        Example3Thread2[] arrayt2 = new Example3Thread2[10];
        t1.start();
        for(int i = 0; i < 10; i++) {
           arrayt2[i] = new Example3Thread2(lock, c, i+2);
            arrayt2[i].start();
        }
    }
}

