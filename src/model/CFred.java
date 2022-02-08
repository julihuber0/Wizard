package model;

public class CFred extends Thread{

    public CFred() {
        super();
    }

    public void warte() throws InterruptedException {
        super.wait();
    }
}
