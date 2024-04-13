package com.crawler;

import org.jsoup.Connection.Response;

public class Crawler {
    public static void main(String[] args) {
        String seedPath = "SpiderDuck/src/main/java/com/crawler/seed.txt";
        String docPath = "SpiderDuck/src/main/java/com/crawler/Docs/";
        Frontier frontier = new Frontier();
        frontier.readSeed(seedPath);
        int i = 0;
        while (!frontier.isEmpty()) {
            Url url = frontier.getNexturl();
            Response response = HttpRequest.sendRequest(url.getUrl());
            if (response == null) {
                i++;
                continue;
            }
            if (!HttpRequest.isHtml(response)) {
                i++;
                continue;
            }
            if (!url.exists()) {
                i++;
                continue;
            }
            // url = url.getNormalized();
            // if (url.robotsExists()) {
            //     RobotsHandler.canBeCrawled(url);
            // }
            HtmlDocument doc = new HtmlDocument(url.getUrl());
            // String hashCode = doc.hash();
            //check if the hashcode exists
            doc.download(docPath + i + ".html");
            i++;
        }
    }

}
// read seeds
// get nextUrl
// process it
// // 1. sendRequest
// // 2. check if url exists --> return if not
// // 3. check if isHtml --> return if not
// // 4. normalize url
// // 5. check if repeated --> return if repeated
// // 6. check if robots.txt exists
// // // // 1. check if the doc can be crawled --> return if not
// // 7. hash the doc
// // 8. check if doc is repeated --> return if repeated
// // 9. download the doc
// // 10. add th url to the frontier