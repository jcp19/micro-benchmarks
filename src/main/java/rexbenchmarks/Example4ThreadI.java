package rexbenchmarks;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Example4ThreadI extends Thread {
    Counter c;
    public Lock lock;
    int i;

    Example4ThreadI(int i, Counter c) {
        this.i = i;
        this.c = c;
        lock = new ReentrantLock();
    }

    @Override
    public void run() {
        c.x = i;        //write x
        lock.lock();    //Lock Li
        c.x = i;        //write x
        lock.unlock();  //unlock Li
    }
}
