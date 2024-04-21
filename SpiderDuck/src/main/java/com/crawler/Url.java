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
            return statusCode != 404;
        } catch (IOException e) {
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
            System.out.println("Error constucting URI");
        }
    }

    public String getUrl() {
        return url;
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

    public boolean robotsExists() {
        return urlExists(robotsUrl);
    }
}