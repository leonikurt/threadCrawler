package main;

public class MyMonitor {

    MyBuffer buffer;

    public MyMonitor(MyBuffer buffer) {
        this.buffer = buffer;
    }

    public String readFromBuffer(){
        return this.buffer.read();
    }

    public void writeOnBuffer(String content){
        this.buffer.addImage(content);
    }

}
