package main;

public class MyMonitor {

    MyBuffer buffer;


    public MyMonitor(MyBuffer buffer) {
        this.buffer = buffer;
    }

    public synchronized String readFromBuffer(){
        return this.buffer.read();
    }

    public synchronized void writeOnBuffer(String content){
        this.buffer.addImage(content);
    }
}
