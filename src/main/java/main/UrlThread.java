package main;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.logging.Logger;

public class UrlThread extends MyThread {
    private static final Logger LOGGER = Logger.getLogger( UrlThread.class.getName() );

    private MyPool observer;

    public UrlThread(MyMonitor monitorInput, MyMonitor monitorOutput, int number, MyPool observer) {
        super(monitorInput, monitorOutput, number);
        this.observer = observer;
    }

    @Override
    public void run() {
        int qtd = 0;
        try {
            String url = this.monitorInput.readFromBuffer();
            if (url != null) {
                LOGGER.info(url);
                Document document = Jsoup.connect("http://" + url).get();
                for (Element element : document.select("img")) {
                    String imageUrl = element.absUrl("src");
                    this.monitorOutput.writeOnBuffer(imageUrl);
                    qtd++;
                }
                this.observer.urlThreadNotification(qtd);
            }
        } catch (IOException e) {
            LOGGER.warning(e.getMessage());
        }
    }
}