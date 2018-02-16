package rexbenchmarks;

import pt.minha.api.sim.Global;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Example0 implements IExample{
    public void mainMinha(String[] args){
        Counter c = new Counter();
        ThreadE0 t1 = new ThreadE0(c);
        ThreadE0 t2 = new ThreadE0(c);
        t1.start();
        t2.start();
    }

    public void main(String[] args){
        Counter c = new Counter();
        ThreadE0 t1 = new ThreadE0(c);
        ThreadE0 t2 = new ThreadE0(c);
        t1.start();
        t2.start();
    }



}
