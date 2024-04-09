package com.crawler;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;

public class Url {
    private String url;
    private String baseUrl;
    private String normalizedUrl;
    private String robotsUrl;

    private boolean urlExists(String url) {
        try {
            Response response = Jsoup.connect(url).execute();
            int statusCode = response.statusCode();
            return statusCode == 404 ? false : true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Url(String url) {
        try {
            URI uri = new URI(url);
            this.url = url;
            this.baseUrl = uri.getScheme() + "://" + uri.getHost();
            this.normalizedUrl = uri.normalize().toString();
            this.robotsUrl = this.baseUrl + "/robots.txt";
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public String getBase() {
        return baseUrl;
    }

    public String getNormalized() {
        return normalizedUrl;
    }

    public String getRobots() {
        return robotsUrl;
    }

    public boolean exists() {
        return urlExists(url);
    }

    public boolean robotsExists() {
        return urlExists(robotsUrl);
    }

    // //test
    // public static void main(String[] args) {
    // String url = "https://github.com/shehab299/DuckDive";
    // Url urlObj = new Url(url);
    // System.out.println("Does url exist? " + urlObj.exists());
    // System.out.println("Base URL: " + urlObj.getBase());
    // System.out.println("Normalized URL: " + urlObj.getNormalized());
    // System.out.println("Does robots.txt exist? " + urlObj.robotsExists());
    // System.out.println("Robots.txt URL: " + urlObj.getRobots());
    // }
}