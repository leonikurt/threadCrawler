package main;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

public class MyBuffer {
    PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private List<String> images;

    public MyBuffer(List<String> images) {
        this.images = images;
    }

    public MyBuffer(List<String> images, PropertyChangeListener listener) {
        this.images = images;
        this.propertyChangeSupport.addPropertyChangeListener("new", listener);
    }

    public String read() {
        if (images.size() > 0)
            return images.remove(images.size() - 1);
        else
            return null;
    }


    public void addImage(String image) {
        images.add(image);
        this.propertyChangeSupport.firePropertyChange("new", images.size()-1, images.size());
    }

    public List<String> getImages() {
        return images;
    }
}
