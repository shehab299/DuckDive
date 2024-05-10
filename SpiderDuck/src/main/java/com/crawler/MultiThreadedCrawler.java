package com.crawler;

import java.util.Scanner;
import com.crawler.Models.PageService;
import com.crawler.utils.DBManager;
import com.mongodb.client.MongoDatabase;

import java.nio.file.Paths;

public class MultiThreadedCrawler {

    private static MongoDatabase connection = DBManager.connect("mongodb://localhost:27017", "test");
    private static PageService service = new PageService(connection);
    private static Frontier frontier;
    public static String seedPath;
    public static String docPath;

    private static void initializeFrontier() {
        frontier = new Frontier();
        frontier.readSeed(seedPath);
    }

    private static void initializePaths() {

        String currentDirectory = System.getProperty("user.dir");

        seedPath = String.valueOf(Paths.get(currentDirectory, "Resources","seed.txt"));
        docPath = String.valueOf(Paths.get(currentDirectory, "Resources","HtmlPages", " "));

        System.out.println("Seed path: " + seedPath);
        System.out.println("Document path: " + docPath);
        
        docPath = docPath.trim();
    }

    private static int getNumThreads() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of threads: ");

        String input = scanner.nextLine();
        int numThreads = Integer.parseInt(input);

        scanner.close();

        return numThreads;
    }

    public static void main(String[] args) {

        initializePaths();
        initializeFrontier();

        int numThreads = getNumThreads();
        Counter numCrawled = new Counter();

        for (int i = 0; i < numThreads; i++) {
            new Thread(new Crawler(frontier, service, numCrawled)).start();
        }
    }
}

class Counter {
    private int counter;

    public Counter() {
        counter = 0;
    }

    public void increment() {
        counter++;
    }

    public int get() {
        return counter;
    }
}