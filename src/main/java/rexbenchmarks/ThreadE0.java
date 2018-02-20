package rexbenchmarks;

public class ThreadE0 extends Thread {
    Counter c;

    public ThreadE0(Counter c) {
       this.c = c;
    }

    @Override
    public void run() {
        for(int i = 0; i < 10; i++){
            c.x = 1;
        }
    }
}
