package com.crawler;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;

public class Url {
    private final String url;
    private final String baseURL;
    private String normalizedURL;
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
        String semi_normalized = "http://" + host + objectURI.getPath();
                
        if(semi_normalized.charAt(semi_normalized.length() - 1) == '/')
            this.normalizedURL = semi_normalized.substring(0,semi_normalized.length()-1);
        else
            this.normalizedURL = semi_normalized;

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