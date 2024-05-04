package com.crawler;

import java.util.List;

import org.jsoup.Connection.Response;

import com.crawler.Models.Page;
import com.crawler.Models.PageService;

public class Crawler implements Runnable {
    private static final int CRAWLING_LIMIT = 5000;
    private static final String seedPath = "SpiderDuck/Resources/seed.txt";
    private static final String docPath = "SpiderDuck/src/main/java/com/crawler/HtmlPages/";
    private static PageService service;
    private Frontier frontier;
    private Counter numCrawled;

    public Crawler(Frontier frontier, PageService service, Counter numCrawled) {
        this.frontier = frontier;
        Crawler.service = service;
        this.numCrawled = numCrawled;

    }

    private static boolean shouldBeCrawled(Response response, Url url) {
        if (service.urlExists(url)) {
            System.out.println(url.getUrl() + " Repeated\n");
        }
        return response != null
                && response.statusCode() == 200
                && HttpRequest.isHtml(response)
                && (!url.robotsExists() || RobotsHandler.canBeCrawled(url))
                && !service.urlExists(url);
    }

    private static void addUrlsToFrontier(HtmlDocument doc, Frontier frontier) {
        Url[] extractedUrls = doc.extractUrls();
        for (Url exctractedUrl : extractedUrls) {
            frontier.addurl(exctractedUrl);
        }
    }

    public void run() {
        synchronized (frontier) {
            frontier.readSeed(seedPath);
        }

        while (true) {
            Url url;
            synchronized (frontier) {
                url = frontier.getNexturl();
            }

            Response response = HttpRequest.sendRequest(url.getUrl());

            if (!shouldBeCrawled(response, url)) {
                continue;
            }

            HtmlDocument doc = new HtmlDocument(url.getUrl());

            String hashCode = doc.hash();

            if (hashCode == null || service.hashExists(hashCode)) {
                continue;
            }

            String path;
            synchronized (numCrawled) {
                path = docPath + numCrawled.get() + ".html";
                numCrawled.increment();
            }

            doc.download(path);

            synchronized (frontier) {
                addUrlsToFrontier(doc, frontier);
            }

            List<String> outlinks = service.getOutlinks(doc.extractUrls());
            Page x = new Page(url.getNormalized(), hashCode, path, outlinks, false, false);
            service.insertPage(x);

            synchronized (numCrawled) {
                if (numCrawled.get() == CRAWLING_LIMIT) {
                    break;
                }
            }
        }
    }
}