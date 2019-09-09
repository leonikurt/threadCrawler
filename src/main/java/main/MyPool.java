package main;

import java.util.List;

public class MyPool extends Thread{

    List<MyThread> threads;
    long timeSleep;

    public MyPool(List<MyThread>  threads, long timeSleep) {
        this.threads = threads;
        this.timeSleep = timeSleep;
    }


    @Override
    public void run() {
        while (true) {
            if (threads.get(0).isBufferEmpty()) {
                if (timeSleep < 1)
                    return;
                try {
                    System.out.println("Sleeping thread");
                    Thread.sleep(timeSleep);
                    timeSleep = timeSleep / 2;
                    System.out.println("Thread awake");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else{
                for (MyThread thread: threads) {
                    if(!thread.isAlive()){
                        thread.run();
                    }
                }
            }
        }
    }
}
