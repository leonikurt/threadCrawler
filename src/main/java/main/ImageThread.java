package main;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

public class ImageThread extends MyThread {

    private final ImagePool observer;
    private static final Logger LOGGER = Logger.getLogger(ImageThread.class.getName());

    public ImageThread(MyMonitor monitorInput, MyMonitor monitorOutput, int number, ImagePool observer) {
        super(monitorInput, monitorOutput, number);
        this.observer = observer;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            LOGGER.info("Starting Image thread " + super.getNumber());
            String imageUrl = this.monitorInput.readFromBuffer();
            if (imageUrl != null) {
                try {
                    imageUrl = new UrlHelper().parseURL(imageUrl);
                    Connection.Response resultImageResponse = Jsoup.connect(imageUrl).ignoreContentType(true).execute();
                    File file = File.createTempFile("image", ".png");
                    file = new File(file.getName());
                    try (FileOutputStream out = (new FileOutputStream(file))) {
                        out.write(resultImageResponse.bodyAsBytes());
                    }
                } catch (IOException e) {
                    LOGGER.warning(e.getMessage());
                }
            } else {
                return;
            }
        }
        this.observer.threadImageNotification(this.getNumber());

    }
}
