package main;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.logging.Logger;

public class UrlThread extends MyThread {
    private static final Logger LOGGER = Logger.getLogger(UrlThread.class.getName());


    public UrlThread(MyMonitor monitorInput, MyMonitor monitorOutput, int number) {
        super(monitorInput, monitorOutput, number);
    }

    @Override
    public void run() {
        LOGGER.info("Starting Url thread " + super.getNumber());
        while (!Thread.currentThread().isInterrupted()) {
            try {
                String url = this.monitorInput.readFromBuffer();
                if (url != null) {
                    LOGGER.info(url);
                    url = new UrlHelper().parseURL(url);
                    Document document = Jsoup.connect(url).get();
                    Elements images = document.select("img[src~=(?i)\\.(png|jpe?g|gif)]");
                    for (Element image : images) {
                        String imageUrl = image.absUrl("src");
                        this.monitorOutput.writeOnBuffer(imageUrl);
                    }
                }
            } catch (IOException e) {
                LOGGER.warning(e.getMessage());
            }
        }
    }
}