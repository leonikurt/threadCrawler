package main;

import java.util.List;

public class UrlPool extends MyPool {

    private int qtd;

    public UrlPool(int qtd){
        this.qtd = qtd;
    }

    public void notify(int threadNumber){
        if(qtd > 0) {
            threads.get(threadNumber).start();
            qtd--;
        }else{
            super.interrupt();
        }
    }

    @Override
    public void run() {
        threads.forEach(Thread::start);
        qtd = qtd - threads.size();
    }

    public List<MyThread> getThreads() {
        return threads;
    }

    public void setThreads(List<MyThread> threads) {
        this.threads = threads;
    }
}
