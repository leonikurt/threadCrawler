package main;

import org.jsoup.Jsoup;

import java.io.IOException;

public class ImageThread extends MyThread {


    public ImageThread(MyMonitor monitorInput, MyMonitor monitorOutput) {
        super(monitorInput, monitorOutput);
    }

    @Override
    public void run() {
        String imageUrl = this.monitorInput.readFromBuffer();
        if(imageUrl != null){
            try {
                String data = Jsoup.connect(imageUrl).ignoreContentType(true).execute().body();
                this.monitorOutput.writeOnBuffer(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
