package com.crawler;

import org.jsoup.Jsoup;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;

import java.io.IOException;

public class HttpRequest {
    public static Response sendRequest(String url) {
        try {
            return Jsoup.connect(url).execute();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isHtml(Response response) {
        String contentType = response.contentType();
        return contentType.startsWith("text/html") ? true : false;
    }

    // not working hehe
    public static String getLastModified(String url) {
        try {
            Connection.Response response = Jsoup.connect(url).method(Connection.Method.HEAD).execute();
            String lastModified = response.header("last-modified");
            return lastModified;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}