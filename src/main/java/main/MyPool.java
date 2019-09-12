package main;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MyPool implements PropertyChangeListener {

    private List<MyThread> threads;
    private int count = 0;
    private Queue<Integer> threadsWaiting = new LinkedList<>();
    private Queue<Integer> urlsWaiting = new LinkedList<>();

    public MyPool() {
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println("listener working...");
        if (evt.getPropertyName().equals("new")) {
            if (count < threads.size()) {
                this.startThread(this.threadsWaiting.peek());
            } else {
                this.urlsWaiting.add(0);
            }
        } else if (evt.getPropertyName().equals("finished")) {
            if (!urlsWaiting.isEmpty()) {
                urlsWaiting.peek();
                int threadNumber = (int) evt.getOldValue();
                this.startThread(threadNumber);
            } else {
                count--;
            }

        }
    }

    private void startThread(int threadNumber) {
        this.threads.get(threadNumber).run();
    }

    public void setThreads(List<MyThread> threads) {
        this.threads = threads;
    }

//    @Override
    public void run() {
        this.threads.forEach(Thread::run);
    }
}
