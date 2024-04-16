package com.crawler;

import org.jsoup.Connection.Response;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Crawler {

    private static void writeToLogFile(String filePath, String message) {
        if (message == null) {
            return;
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(message);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        String logFilePath = "SpiderDuck/src/main/java/com/crawler/CrawlerLog.txt";

        String seedPath = "SpiderDuck/src/main/java/com/crawler/seed.txt";
        String docPath = "SpiderDuck/src/main/java/com/crawler/Docs/";

        Frontier frontier = new Frontier();
        frontier.readSeed(seedPath);
        int i = 0;
        while (i < 500) {
            writeToLogFile(logFilePath, "-----------------------");
            Url url = frontier.getNexturl();

            writeToLogFile(logFilePath, Integer.toString(i));

            Response response = HttpRequest.sendRequest(url.getUrl());

            writeToLogFile(logFilePath, "Response? " + response);

            if (response == null) {
                i++;
                continue;
            }

            writeToLogFile(logFilePath, "Is Html? " + HttpRequest.isHtml(response));
            if (!HttpRequest.isHtml(response)) {
                i++;
                continue;
            }

            writeToLogFile(logFilePath, "Url exists?" + url.exists());
            if (!url.exists()) {
                i++;
                continue;
            }

            // url = url.getNormalized();
            writeToLogFile(logFilePath, "Robots exists? " + url.robotsExists());
            writeToLogFile(logFilePath, "Can be crawled? " + RobotsHandler.canBeCrawled(url));
            if (url.robotsExists()) {
                if (!RobotsHandler.canBeCrawled(url)) {
                    i++;
                    continue;
                }
            }

            HtmlDocument doc = new HtmlDocument(url.getUrl());
            String hashCode = doc.hash();

            writeToLogFile(logFilePath, "hashCode: " + hashCode);

            doc.download(docPath + i + ".html");
            System.out.println("\n\n\n\n\n");
            Url[] exctractedUrls = doc.extractUrls();
            for (Url exctractedUrl : exctractedUrls) {
                System.out.println(exctractedUrl.getUrl());
                writeToLogFile(logFilePath, exctractedUrl.getUrl());
                frontier.addurl(exctractedUrl);
            }
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
// // 10. add the url to the frontier