package com.search_engine;

import java.lang.Thread;
import com.crawler.MultiThreadedCrawler;
// import com.search_engine.Runnables.IndexerRunnable;
import com.search_engine.Runnables.PageRankRunnable;

public class Starter {
    public static void main(String[] args) {
        MultiThreadedCrawler.main(args);

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

            @Override
            public void run() {
                System.out.println("Shutting down the crawler...");
                // Thread indexThread = new Thread(new IndexerRunnable());
                Thread pageRankThread = new Thread(new PageRankRunnable());

                // indexThread.start();
                pageRankThread.start();
                try {
                    // indexThread.join();
                    pageRankThread.join();
                } catch (InterruptedException e) {
                    System.out.println("Error in Starter");
                }
            }
        }));

    }
}
