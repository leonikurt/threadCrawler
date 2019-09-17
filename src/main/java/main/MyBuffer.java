package main;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class MyBuffer {

    private static final Logger LOGGER = Logger.getLogger(MyBuffer.class.getName());
    private ImagePool observer;
    private List<String> images;

    public MyBuffer(List<String> images, ImagePool observer) {
        this.images = Collections.synchronizedList(images);
        this.observer = observer;
    }

    public MyBuffer(List<String> images) {
        this.images = images;
    }

    public String read() {
        synchronized (this) {
            if (!images.isEmpty()) {
                try {
                    return images.remove(images.size() - 1);
                } catch (Exception e) {
                    LOGGER.warning(e.getMessage());
                    return null;
                }
            }
            return null;
        }
    }

    public void addImage(String image) {
        images.add(image);
        this.observer.bufferNotification();
    }

    public List<String> getImages() {
        return images;
    }
}
