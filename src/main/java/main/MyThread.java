package main;

public abstract class MyThread implements Runnable {

  MyMonitor monitorInput;
  MyMonitor monitorOutput;


    public MyThread(MyMonitor monitorInput, MyMonitor monitorOutput) {
        this.monitorInput = monitorInput;
        this.monitorOutput = monitorOutput;
    }
}
