package main;

import java.util.ArrayList;
import java.util.List;

public class MyBuffer {
    private List<String> images;

    public MyBuffer(List<String> images) {
        this.images = images;
    }

    public String read(){
        if(images.size() > 0)
            return images.remove(images.size() - 1);
        else
            return null;
    }


    public void addImage(String image){
        images.add(image);
    }
}
