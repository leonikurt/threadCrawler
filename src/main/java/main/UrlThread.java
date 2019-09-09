package main;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.logging.Logger;

public class UrlThread extends MyThread {

    public UrlThread(MyMonitor monitorInput, MyMonitor monitorOutput) {
        super(monitorInput, monitorOutput);
    }

    @Override
    public boolean isBufferEmpty() {
        return this.monitorInput.isBufferEmpty();
    }

    @Override
    public void run() {
        try {
            String url = this.monitorInput.readFromBuffer();
            if(url != null) {
                Document document = Jsoup.connect("http://" + url).get();
                for (Element element : document.select("img")) {
                    String imageUrl = element.absUrl("src");
                    this.monitorOutput.writeOnBuffer(imageUrl);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}