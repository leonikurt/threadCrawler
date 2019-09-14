package main;

import java.util.Collections;
import java.util.List;

public class MyBuffer {
    private MyPool observer;
    private List<String> images;

    public MyBuffer(List<String> images, MyPool observer) {

        this.images = Collections.synchronizedList(images);
        this.observer = observer;

    }

    public MyBuffer(List<String> images) {
        this.images = images;
    }

    public String read() {
        if (!images.isEmpty())
            return images.remove(images.size() - 1);
        else
            return null;
    }

    public void addImage(String image) {
        images.add(image);
        this.observer.bufferNotification();
    }

    public List<String> getImages() {
        return images;
    }
}
