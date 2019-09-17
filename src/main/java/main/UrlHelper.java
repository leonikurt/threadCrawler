package main;

public class UrlHelper {

    public String parseURL(String url){
        if (url.length() > 4 && !url.substring(0, 4).equals("http")) {
            url = "http:" + url;
        }
        return url;
    }
}
