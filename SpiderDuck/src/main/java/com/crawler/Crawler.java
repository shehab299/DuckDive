package com.crawler;

import java.util.List;

import org.jsoup.Connection.Response;

import com.crawler.Models.Page;
import com.crawler.Models.PageService;

public class Crawler implements Runnable {
    private static final int CRAWLING_LIMIT = 5000;
    private static int SAVING_FREQUENCY;
    private PageService service;
    private Frontier frontier;
    private Counter numCrawled;

    public Crawler(Frontier frontier, PageService service, Counter numCrawled, int saveFrequency) {
        this.frontier = frontier;
        this.service = service;
        this.numCrawled = numCrawled;
        SAVING_FREQUENCY = saveFrequency;

    }

    private boolean shouldBeCrawled(Response response, Url url) {

        if (service.urlExists(url)) {
            System.out.println(url.getNormalized() + " Repeated\n");
            return false;
        }

        return response != null
                && response.statusCode() == 200
                && HttpRequest.isHtml(response);
        // && !url.robotsExists()
        // && RobotsHandler.canBeCrawled(url);

    }

    private static void addUrlsToFrontier(HtmlDocument doc, Frontier frontier) {
        Url[] extractedUrls = doc.extractUrls();
        for (Url exctractedUrl : extractedUrls) {
            frontier.addurl(exctractedUrl);
        }
    }

    public void run() {

        while (true) {
            synchronized (numCrawled) {
                if (numCrawled.get() >= CRAWLING_LIMIT) {
                    break;
                }

                if (numCrawled.get() != 0 && numCrawled.get() % SAVING_FREQUENCY == 0) {
                    frontier.serialize(MultiThreadedCrawler.serialPath);
                }
            }

            Url url;
            synchronized (frontier) {
                if (frontier.isEmpty())
                    break;

                url = frontier.getNexturl();
            }

            Response response = HttpRequest.sendRequest(url.getNormalized());

            if (!shouldBeCrawled(response, url)) {
                continue;
            }

            HtmlDocument doc = new HtmlDocument(url.getNormalized());

            String hashCode = doc.hash();

            if (hashCode == null || service.hashExists(hashCode)) {
                continue;
            }

            String path;

            synchronized (numCrawled) {
                numCrawled.increment();
                path = MultiThreadedCrawler.docPath + numCrawled.get() + ".html";
            }

            doc.download(path);

            synchronized (frontier) {
                addUrlsToFrontier(doc, frontier);
            }

            List<String> outlinks = service.getOutlinks(doc.extractUrls());
            Page x = new Page(url.getNormalized(), url.getUrl(), hashCode, path,
                    outlinks, false, false);
            service.insertPage(x);

        }
    }
}