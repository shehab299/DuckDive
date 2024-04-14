package com.crawler;

import java.util.LinkedList;
import java.util.Queue;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Frontier {
    private Queue<Url> urlQueue;
    private int count;

    public Frontier() {
        urlQueue = new LinkedList<Url>();
        count = 0;
    }

    public void readSeed(String seedPath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(seedPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Url url = new Url(line);
                addurl(url);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void addurl(Url url) {
        urlQueue.offer(url);
        count++;
    }

    public Url getNexturl() {
        if (count > 0)
            count--;
        return urlQueue.poll();
    }


    public boolean isEmpty() {
        return count == 0;
    }

}

