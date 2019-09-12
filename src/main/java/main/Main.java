package main;


import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

import static java.nio.file.Files.*;

public class Main {

    public static void main(String[] args) {
        String filePath = "/home/felipe/Desktop/workspace/java/crawler/file.json";
        String[] links = parseJson(filePath);
        List<String> urls = new ArrayList<>(Arrays.asList(links));
        List<String> images = new ArrayList<>();
        List<String> response = new ArrayList<>();


        int poolSize = 2;
        MyPool urlPool = new MyPool();
        MyPool imagePool = new MyPool();

        MyMonitor urlMonitor = new MyMonitor(new MyBuffer(urls, imagePool));
        MyMonitor imagesMonitor = new MyMonitor(new MyBuffer(images));
        MyMonitor outputMonitor = new MyMonitor(new MyBuffer(response));

        List<MyThread> urlThreads = createUrlRunnables(urlMonitor, imagesMonitor, poolSize / 2);
        List<MyThread> imageThreads = createImageRunnables(imagesMonitor, outputMonitor, poolSize / 2, imagePool);

        urlPool.setThreads(urlThreads);
        imagePool.setThreads(imageThreads);
        urlPool.run();
//            imagePool.run();

    }

    public static List<MyThread> createUrlRunnables(MyMonitor monitorInput, MyMonitor monitorOutput, int size) {
        List<MyThread> runnables = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            try {
                runnables.add(new UrlThread(monitorInput, monitorOutput, i));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return runnables;
    }

    public static List<MyThread> createImageRunnables(MyMonitor monitorInput, MyMonitor monitorOutput, int size, MyPool pool) {
        List<MyThread> runnables = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            try {
                runnables.add(new ImageThread(monitorInput, monitorOutput, i, pool));
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
