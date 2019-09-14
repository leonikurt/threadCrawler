package main;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageThread extends MyThread {

    private final MyPool observer;

    public ImageThread(MyMonitor monitorInput, MyMonitor monitorOutput, int number, MyPool observer) {
        super(monitorInput, monitorOutput, number);
        this.observer = observer;
    }

    @Override
    public void run() {
        String imageUrl = this.monitorInput.readFromBuffer();
        if (imageUrl != null) {
            try {
                Connection.Response resultImageResponse = Jsoup.connect(imageUrl).ignoreContentType(true).execute();
                File file = File.createTempFile("image", ".png");
                file = new File(file.getName());
                try (FileOutputStream out = (new FileOutputStream(file))) {
                    out.write(resultImageResponse.bodyAsBytes());
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                this.observer.threadNotification(this.getNumber());
            }
        }else{
            System.out.println("aaaaaaa");
        }

    }
}
