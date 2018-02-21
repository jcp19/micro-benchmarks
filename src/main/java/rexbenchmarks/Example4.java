package rexbenchmarks;

public class Example4 implements IExample {
    public void mainMinha(String[] args) {
       Example4ThreadI[] tis = new Example4ThreadI[4];

       Counter c = new Counter();
       for(int i=1; i<=3; i++) {
           tis[i] = new Example4ThreadI(i,c);
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
       Example4ThreadI[] tis = new Example4ThreadI[4];

       Counter c = new Counter();
       for(int i=1; i<=3; i++) {
           tis[i] = new Example4ThreadI(i,c);
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

