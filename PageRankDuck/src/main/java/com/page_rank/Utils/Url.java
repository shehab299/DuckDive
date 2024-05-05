package com.page_rank.Utils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;

public class Url {
    private final String url;
    private final String baseURL;
    private final String normalizedURL;
    private final String robotsURL;

    private boolean urlExists(String url) {
        try {
            Response response = Jsoup.connect(url).execute();
            int statusCode = response.statusCode();
            return statusCode != 404;
        } catch (IOException e) {
            return false;
        }
    }

    public Url(String url) throws URISyntaxException {

        URI objectURI = new URI(url);
        objectURI = objectURI.normalize();
        String host = objectURI.getHost();

        if(host == null)
            throw new URISyntaxException(url,"Couldn't Resolve Host");

        host = host.replaceFirst("www.","");
        this.url = url;
        this.normalizedURL = "http://" + host + objectURI.getPath();
        this.baseURL = "http://" + host;
        this.robotsURL = this.baseURL + "/robots.txt";
    }


    public String getUrl() {
        return url;
    }

    public String getBase() {
        return baseURL;
    }

    public String getNormalized() {
        return normalizedURL;
    }

    public String getRobots() {
        return robotsURL;
    }

    public boolean robotsExists() {
        return urlExists(robotsURL);
    }

    @Override
    public String toString() {
        return "Url{" +
                "url='" + url + '\'' +
                ", baseUrl='" + baseURL + '\'' +
                ", normalizedUrl='" + normalizedURL + '\'' +
                ", robotsUrl='" + robotsURL + '\'' +
                '}';
    }
}