package com.crawler;

import java.util.Scanner;

import org.bson.Document;

import com.crawler.Models.PageService;
import com.crawler.utils.DBManager;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import java.lang.Thread;

public class MultiThreadedCrawler {

    private static MongoDatabase connection = DBManager.connect("mongodb://localhost:27017", "DummySearchEngine");
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

        // seedPath = String.valueOf(Paths.get(currentDirectory, "Resources",
        // "seed.txt"));
        seedPath = "D:\\CMP_Projects\\CMP_Year2\\APT\\DuckDive\\SpiderDuck\\Resources\\seed.txt";
        // docPath = String.valueOf(Paths.get(currentDirectory, "Resources",
        // "HtmlPages", " "));
        docPath = "D:\\CMP_Projects\\CMP_Year2\\APT\\DuckDive\\SpiderDuck\\Resources\\HtmlPages\\";
        // docPath = docPath.trim();

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

        MongoCollection<Document> pageTable = connection.getCollection("page");
        DeleteResult result = pageTable.deleteMany(new Document());
        System.out.println("Deleted " + result.getDeletedCount() + " documents");

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