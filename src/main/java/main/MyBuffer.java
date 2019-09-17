package main;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class MyBuffer {

    private static final Logger LOGGER = Logger.getLogger(MyBuffer.class.getName());
    private List<String> images;

    public MyBuffer(List<String> images) {
        this.images = Collections.synchronizedList(images);
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
    }

    public List<String> getImages() {
        return images;
    }
}
