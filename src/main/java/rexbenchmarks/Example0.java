package rexbenchmarks;

public class Example0 {

    public void main(String[] args){
        Counter c = new Counter();
        Example0Thread t1 = new Example0Thread(c);
        Example0Thread t2 = new Example0Thread(c);
        t1.start();
        t2.start();
    }
}
