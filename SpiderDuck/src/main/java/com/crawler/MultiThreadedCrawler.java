package com.crawler;

import java.util.Scanner;

import org.bson.Document;

import com.crawler.Models.PageService;
import com.crawler.utils.DBManager;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.internal.bulk.DeleteRequest;

public class MultiThreadedCrawler {
    static MongoDatabase connection = DBManager.connect("mongodb://localhost:27017", "SearchEngine");
    static PageService service = new PageService(connection);
    static Frontier frontier = new Frontier();

    private static int getNumThreads() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of threads: ");

        String input = scanner.nextLine();
        int numThreads = Integer.parseInt(input);

        scanner.close();

        return numThreads;
    }

    public static void main(String[] args) {
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