package main;


import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Logger;

import static java.nio.file.Files.*;

public class Main {

    private static final Logger LOGGER = Logger.getLogger( Main.class.getName() );
    public static void main(String[] args) {
        String filePath = "/home/felipe/Desktop/workspace/java/crawler/file.json";
        if(args.length >= 1){
            filePath = args[0];
        }

        String[] links = parseJson(filePath);
        List<String> urls = new ArrayList<>(Arrays.asList(links));
        List<String> images = new ArrayList<>();
        List<String> response = new ArrayList<>();

        int poolSize = 13;
        UrlPool urlPool = new UrlPool(links.length);
        ImagePool imagePool = new ImagePool();

        MyMonitor urlMonitor = new MyMonitor(new MyBuffer(urls, imagePool));
        MyMonitor imagesMonitor = new MyMonitor(new MyBuffer(images, imagePool));
        MyMonitor outputMonitor = new MyMonitor(new MyBuffer(response));

        List<MyThread> urlThreads = createUrlRunnables(urlMonitor, imagesMonitor, links.length, urlPool);
        List<MyThread> imageThreads = createImageRunnables(imagesMonitor, outputMonitor, poolSize, imagePool);

        urlPool.setThreads(urlThreads);
        imagePool.setThreads(imageThreads);
        urlPool.start();

    }

    private static List<MyThread> createUrlRunnables(MyMonitor monitorInput, MyMonitor monitorOutput, int size, UrlPool pool) {
        List<MyThread> runnables = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            try {
                runnables.add(new UrlThread(monitorInput, monitorOutput, i, pool));
            } catch (Exception e) {
                LOGGER.warning(e.getMessage());
            }
        }
        return runnables;
    }

    private static List<MyThread> createImageRunnables(MyMonitor monitorInput, MyMonitor monitorOutput, int size, ImagePool pool) {
        List<MyThread> runnables = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            try {
                runnables.add(new ImageThread(monitorInput, monitorOutput, i, pool));
            } catch (Exception e) {
                LOGGER.warning(e.getMessage());
            }
        }
        return runnables;
    }


    private static String[] parseJson(String filePath) {
        String json = null;
        try {
            json = readString(Paths.get(filePath));
        } catch (IOException e) {
            LOGGER.warning(e.getMessage());
        }

        LOGGER.info(json);
        return new Gson().fromJson(json, String[].class);
    }


}
