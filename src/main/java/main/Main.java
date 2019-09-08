package main;


import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.nio.file.Files.*;

public class Main {

    public static void main(String[] args) {
        String filePath = "/home/felipe/Desktop/workspace/java/crawler/file.json";
        if (args.length == 1) {
            filePath = args[0];
        } else {
            String[] links = parseJson(filePath);
            int poolSize = 10;

            ExecutorService urlPool = Executors.newFixedThreadPool(poolSize);
            ExecutorService imagePool = Executors.newFixedThreadPool(poolSize);


            List<String> urls = new ArrayList<>(Arrays.asList(links));
            List<String> images = new ArrayList<>();

            MyMonitor urlMonitor = new MyMonitor(new MyBuffer(urls));
            MyMonitor imagesMonitor = new MyMonitor(new MyBuffer(images));
            MyMonitor outputMonitor = new MyMonitor(new MyBuffer(new ArrayList<>()));

            List<Runnable> urlThreads = createUrlRunnables(urlMonitor, imagesMonitor, poolSize / 2);
            List<Runnable> imageThreads = createImageRunnables(imagesMonitor, outputMonitor, poolSize / 2);

            urlThreads.forEach(urlPool::execute);
            imageThreads.forEach(imagePool::execute);
            urlPool.shutdown();
            imagePool.shutdown();

        }
    }

    public static List<Runnable> createUrlRunnables(MyMonitor monitorInput, MyMonitor monitorOutput, int size) {
        List<Runnable> runnables = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            try {
                runnables.add(new UrlThread(monitorInput, monitorOutput));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return runnables;
    }

    public static List<Runnable> createImageRunnables(MyMonitor monitorInput, MyMonitor monitorOutput, int size) {
        List<Runnable> runnables = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            try {
                runnables.add(new ImageThread(monitorInput, monitorOutput));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return runnables;
    }


    public static String[] parseJson(String filePath) {
        String json = null;
        try {
            json = readString(Paths.get(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(json);
        return new Gson().fromJson(json, String[].class);
    }


}
