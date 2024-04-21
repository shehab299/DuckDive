package com.crawler;

import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;

public class HttpRequest {
    public static Response sendRequest(String url) {
        try {
            return Jsoup.connect(url).execute();
        } catch (Exception e) {
            System.out.println("Can't Connect To " + url);
            return null;
        }
    }

    public static boolean isHtml(Response response) {
        String contentType = response.contentType();
        assert contentType != null;
        return contentType.startsWith("text/html");
    }
}