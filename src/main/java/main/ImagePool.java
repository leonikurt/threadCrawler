package main;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ImagePool extends MyPool implements Runnable {

    private int countUrlsWaiting = 0;
    private Queue<Integer> threadsWaiting = new LinkedList<>();


    public void threadImageNotification(int threadNumber) {
        synchronized (this) {
            countUrlsWaiting--;
            if (countUrlsWaiting > 0) {
                this.threads.get(threadNumber).start();
            } else {
                if(!this.threadsWaiting.contains(threadNumber)){
                    this.threadsWaiting.add(threadNumber);
                }
            }
        }
    }

    public void bufferNotification() {
        synchronized (this) {
            this.countUrlsWaiting++;
            if (!this.threadsWaiting.isEmpty()) {
                int number = this.threadsWaiting.remove();
                threads.get(number).start();
            }
        }
    }


    public void setThreads(List<MyThread> threads) {
        this.threads = threads;
        for (int i = 0; i < this.threads.size(); i++) {
            threadsWaiting.add(i);
        }
    }

    @Override
    public void run() { this.threads.forEach(Thread::start);}

}
