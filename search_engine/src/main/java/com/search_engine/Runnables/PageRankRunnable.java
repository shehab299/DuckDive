package com.search_engine.Runnables;

import java.net.URISyntaxException;
import com.page_rank.PageRank;

public class PageRankRunnable implements Runnable {

    @Override
    public void run() {
        try {
            PageRank.main(new String[] {});
        } catch (URISyntaxException e) {
            System.out.println("Error in PageRankRunnable");
        }
    }
}
