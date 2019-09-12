package main;

public abstract class MyThread extends Thread implements Runnable {

    MyMonitor monitorInput;
    MyMonitor monitorOutput;
    private int number;

    public MyThread(MyMonitor monitorInput, MyMonitor monitorOutput, int number) {
        this.monitorInput = monitorInput;
        this.monitorOutput = monitorOutput;
        this.number = number;
    }


    public int getNumber() {
        return number;
    }
}
