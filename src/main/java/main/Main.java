package main;


import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import static java.nio.file.Files.*;

public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    private static final int POOL_NUMBER = 15;

    public static void main(String[] args) {
        String filePath = "/home/felipe/Desktop/workspace/java/crawler/file.json";
        if (args.length >= 1) {
            filePath = args[0];
        }

        String[] links = parseJson(filePath);
        List<String> urls = new ArrayList<>(Arrays.asList(links));
        List<String> images = new ArrayList<>();
        List<String> response = new ArrayList<>();

        ExecutorService imagePool = Executors.newFixedThreadPool(POOL_NUMBER);
        ExecutorService urlPool = Executors.newFixedThreadPool(links.length);

        MyMonitor urlMonitor = new MyMonitor(new MyBuffer(urls));
        MyMonitor imagesMonitor = new MyMonitor(new MyBuffer(images));
        MyMonitor outputMonitor = new MyMonitor(new MyBuffer(response));

        List<MyThread> urlThreads = createUrlRunnables(urlMonitor, imagesMonitor, links.length);
        List<MyThread> imageThreads = createImageRunnables(imagesMonitor, outputMonitor, POOL_NUMBER);
        urlThreads.forEach(urlPool::execute);
        imageThreads.forEach(imagePool::execute);


    }

    private static List<MyThread> createUrlRunnables(MyMonitor monitorInput, MyMonitor monitorOutput, int threadNumbers) {
        List<MyThread> runnables = new ArrayList<>();
        for (int i = 0; i < threadNumbers; i++) {
            try {
                runnables.add(new UrlThread(monitorInput, monitorOutput, i));
            } catch (Exception e) {
                LOGGER.warning(e.getMessage());
            }
        }
        return runnables;
    }

    private static List<MyThread> createImageRunnables(MyMonitor monitorInput, MyMonitor monitorOutput, int threadNumber) {
        List<MyThread> runnables = new ArrayList<>();
        for (int i = 0; i < threadNumber; i++) {
            try {
                runnables.add(new ImageThread(monitorInput, monitorOutput, i));
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
