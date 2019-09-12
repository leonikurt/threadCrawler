package main;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class ImageThread extends MyThread {
    PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public ImageThread(MyMonitor monitorInput, MyMonitor monitorOutput, int number, PropertyChangeListener listener) {
        super(monitorInput, monitorOutput, number);
        this.propertyChangeSupport.addPropertyChangeListener("finished", listener);
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
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                this.propertyChangeSupport.firePropertyChange("finished", this.getNumber(), null);
            }
        }

    }
}
