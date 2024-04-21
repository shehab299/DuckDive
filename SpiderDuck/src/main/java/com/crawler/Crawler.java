package com.crawler;

import com.crawler.Models.Page;
import com.crawler.Models.PageService;
import com.crawler.utils.DBManager;
import com.mongodb.client.MongoDatabase;

import org.jsoup.Connection.Response;

public class Crawler {
    private static final int CRAWLING_LIMIT = 5000;
    private static final String seedPath = "SpiderDuck/Resources/seed.txt";
    private static final String docPath = "SpiderDuck/src/main/java/com/crawler/Pages/";
    private static MongoDatabase connection = DBManager.connect("mongodb://localhost:27017", "SearchEngine");
    private static PageService service = new PageService(connection);

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

    public static void main(String[] args) {

        Frontier frontier = new Frontier();
        frontier.readSeed(seedPath);

        int numCrawled = 0;
        while (numCrawled < CRAWLING_LIMIT) {
            Url url = frontier.getNexturl();

            Response response = HttpRequest.sendRequest(url.getUrl());

            if (!shouldBeCrawled(response, url)) {
                continue;
            }

            HtmlDocument doc = new HtmlDocument(url.getUrl());

            String hashCode = doc.hash();

            if (hashCode == null || service.hashExists(hashCode)) {
                continue;
            }

            String path = docPath + numCrawled + ".html";
            doc.download(path);
            addUrlsToFrontier(doc, frontier);

            Page x = new Page(url.getNormalized(), hashCode, path, false);
            service.insertPage(x);

            numCrawled++;
        }
    }
}