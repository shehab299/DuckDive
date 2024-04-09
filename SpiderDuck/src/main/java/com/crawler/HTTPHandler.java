package com.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class HTTPHandler {
    public static boolean isHtml(String url) {
        try {
            Response response = Jsoup.connect(url).execute();
            String contentType = response.contentType();
            return contentType.startsWith("text/html") ? true : false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void downloadFile(Document document, String path) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write(document.outerHtml());
        } catch (IOException e) {
            System.err.println("Error occurred while saving the document: " + e.getMessage());
        }
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