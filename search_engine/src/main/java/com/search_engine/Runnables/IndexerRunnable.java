package com.search_engine.Runnables;

import com.indexer.Indexer;

public class IndexerRunnable implements Runnable {

    @Override
    public void run() {
        Indexer.main(new String[] {});
    }
}