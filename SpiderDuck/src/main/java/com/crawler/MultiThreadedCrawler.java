package com.crawler;

import java.util.Scanner;

import com.crawler.Models.PageService;
import com.crawler.utils.DBManager;
import com.mongodb.client.MongoDatabase;


import java.nio.file.Paths
import com.mongodb.client.result.DeleteResult;
import java.lang.Thread;


public class MultiThreadedCrawler {

    private static MongoDatabase connection = DBManager.connect("mongodb://localhost:27017", "SearchEngine");
    private static PageService service = new PageService(connection);
    private static Frontier frontier;
    public static String seedPath;
    public static String docPath;
    public static String serialPath;
    public static long crawlerState;
    private static Scanner scanner = new Scanner(System.in);

    private static void initializeFrontier() {
        crawlerState = service.countUrls();
        if (crawlerState != 0) {
            frontier = Frontier.deserialize(serialPath);
            return;
        }

        frontier = new Frontier();
        frontier.readSeed(seedPath);
    }

    private static void initializePaths() {
        String currentDirectory = System.getProperty("user.dir");

        seedPath = String.valueOf(Paths.get(currentDirectory, "Resources", "seed.txt"));
        docPath = String.valueOf(Paths.get(currentDirectory, "Resources",
        "HtmlPages", " "));
        serialPath = String.valueOf(Paths.get(currentDirectory, "Resources", "frontier.txt"));
        docPath = docPath.trim();
    }

    private static int getNumThreads() {
        System.out.print("Enter the number of threads: ");
        int numThreads = scanner.nextInt();
        return numThreads;
    }

    private static int getSaveFrequency() {
        System.out.print("Every how many crawls do you wanna save the state? ");
        int saveFrequency = scanner.nextInt();
        scanner.nextLine();
        return saveFrequency;
    }

    public static void main(String[] args) {

        initializePaths();
        initializeFrontier();

        int numThreads = getNumThreads();
        int saveFrequency = getSaveFrequency();

        Counter numCrawled = new Counter(crawlerState);
        for (int i = 0; i < numThreads; i++) {
            new Thread(new Crawler(frontier, service, numCrawled, saveFrequency)).start();
        }
    }
}

class Counter {
    private long counter;

    public Counter(long crawlerState) {
        counter = crawlerState;
    }

    public void increment() {
        counter++;
    }

    public long get() {
        return counter;
    }
}