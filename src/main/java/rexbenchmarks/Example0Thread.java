package rexbenchmarks;

public class Example0Thread extends Thread {
    Counter c;

    public Example0Thread(Counter c) {
       this.c = c;
    }

    @Override
    public void run() {
        for(int i = 0; i < 10; i++){
            c.x = 1;
        }
    }
}
