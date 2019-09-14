package main;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MyPool implements Runnable {

    private List<MyThread> threads;
    private int countUrlsWaiting = 0;
    private Queue<Integer> threadsWaiting = new LinkedList<>();

    public MyPool() {
    }


    public void threadNotification(int threadNumber) {
        synchronized (this) {
            if (countUrlsWaiting > 0) {
                this.startThread(threadNumber);
            } else {
                this.threadsWaiting.add(threadNumber);
            }
        }
    }

    public void bufferNotification() {
        synchronized (this) {
            if (!this.threadsWaiting.isEmpty()) {
                this.startThread(this.threadsWaiting.peek());
            } else {
                this.countUrlsWaiting++;
            }
        }
    }

    private void startThread(int threadNumber) {
        this.threads.get(threadNumber).run();
    }

    public void setThreads(List<MyThread> threads) {
        this.threads = threads;

        for (int i = 0; i < this.threads.size() ; i++) {
            threadsWaiting.add(i);
        }
    }

    @Override
    public void run() {
        this.threads.forEach(Thread::run);
    }

    public void run(int qtd) {
        for (int i = 0; i < qtd && i < this.threadsWaiting.size(); i++) {
            threads.get(this.threadsWaiting.peek()).run();

        }
    }

    public void urlThreadNotification(int qtd) {
        this.run(qtd);
    }
}
