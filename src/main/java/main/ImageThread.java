package main;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class ImageThread extends MyThread {


    public ImageThread(MyMonitor monitorInput, MyMonitor monitorOutput) {
        super(monitorInput, monitorOutput);
    }

    @Override
    public boolean isBufferEmpty() {
        return this.monitorInput.isBufferEmpty();
    }

    @Override
    public void run() {
        String imageUrl = this.monitorInput.readFromBuffer();
        if (imageUrl != null) {
            try {
                Connection.Response resultImageResponse = Jsoup.connect(imageUrl).ignoreContentType(true).execute();
                try (FileOutputStream out = (new FileOutputStream(new java.io.File(new Date().toString() + ".png")))) {
                    out.write(resultImageResponse.bodyAsBytes());  // resultImageResponse.body() is where the image's contents are.
                }

//                this.monitorOutput.writeOnBuffer(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
