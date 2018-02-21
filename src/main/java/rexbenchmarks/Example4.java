package rexbenchmarks;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Example4 implements IExample {
    public void mainMinha(String[] args) {
       ThreadIEx4[] tis = new ThreadIEx4[4];

       Counter c = new Counter();
       for(int i=1; i<=3; i++) {
           tis[i] = new ThreadIEx4(i,c);
           tis[i].start();
       }
       for(int i=1; i<=3; i++) {
           System.out.println(c.x);     //read x
           tis[i].lock.lock();          //lock Li
           c.x = i;                     //write x
           tis[i].lock.unlock();        //unlock Li
       }
    }

    public static void main(String[] args) {
       ThreadIEx4[] tis = new ThreadIEx4[4];

       Counter c = new Counter();
       for(int i=1; i<=3; i++) {
           tis[i] = new ThreadIEx4(i,c);
           tis[i].start();
       }
       for(int i=1; i<=3; i++) {
           System.out.println(c.x);     //read x
           tis[i].lock.lock();          //lock Li
           c.x = i;                     //write x
           tis[i].lock.unlock();        //unlock Li
       }
    }
}

