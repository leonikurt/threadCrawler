package main;


import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

import static java.nio.file.Files.*;

public class Main {

    public static void main(String[] args) {
        String filePath = "/home/felipe/Desktop/workspace/java/crawler/file.json";
        if (args.length == 1) {
            filePath = args[0];
        } else {
            String[] links = parseJson(filePath);
            int poolSize = 10;
//
//            ExecutorService urlPool = Executors.newFixedThreadPool(poolSize);
//            ExecutorService imagePool = Executors.newFixedThreadPool(poolSize);


            List<String> urls = new ArrayList<>(Arrays.asList(links));
            List<String> images = new ArrayList<>();
            List<String> response = new ArrayList<>();

            MyMonitor urlMonitor = new MyMonitor(new MyBuffer(urls));
            MyMonitor imagesMonitor = new MyMonitor(new MyBuffer(images));
            MyMonitor outputMonitor = new MyMonitor(new MyBuffer(response));

            List<MyThread> urlThreads = createUrlRunnables(urlMonitor, imagesMonitor, poolSize / 2);
            List<MyThread> imageThreads = createImageRunnables(imagesMonitor, outputMonitor, poolSize / 2);

            MyPool urlPool = new MyPool(urlThreads, 2000);
            MyPool imagePool = new MyPool(imageThreads, 2000);
            urlPool.run();
            imagePool.run();

//            urlThreads.forEach(urlPool::execute);
//            imageThreads.forEach(imagePool::execute);


        }
    }

    public static List<MyThread> createUrlRunnables(MyMonitor monitorInput, MyMonitor monitorOutput, int size) {
        List<MyThread> runnables = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            try {
                runnables.add(new UrlThread(monitorInput, monitorOutput));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return runnables;
    }

    public static List<MyThread> createImageRunnables(MyMonitor monitorInput, MyMonitor monitorOutput, int size) {
        List<MyThread> runnables = new ArrayList<>();
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
