package com.crawler;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;

public class Frontier {
    private HashMap <String, Queue<Url>> urlDict;
    private List<Url> hostList;
    private int count;
    private int index;

    public Frontier() {
        urlDict = new HashMap<String, Queue<Url>>();
        hostList = new LinkedList<Url>();
        count = 0;
        index = 0;
    }

    public void readSeed(String seedPath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(seedPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Url url;
                try {
                    url = new Url(line);
                } catch (URISyntaxException e) {
                    continue;
                }
                addurl(url);
            }
        } catch (IOException e) {
            return;
        }
    }

    public void addurl(Url url) {

        String host = url.getHost();
        
        if (!urlDict.containsKey(host)) {
            urlDict.put(host, new LinkedList<Url>());
            hostList.add(url);
        }

        count++;
        urlDict.get(host).offer(url);
    }

    public Url getNexturl() {

        index = (index + 1) % hostList.size();

        int limit = hostList.size();

        for (int i = index; i < limit;) {

            String host = hostList.get(i).getHost();

            if (!urlDict.get(host).isEmpty()) {
                count--;
                return urlDict.get(host).poll();
            }

            i = (i + 1) % hostList.size();

            if(i == 0){
                limit = index;
            }
        }

        return null;
    }
  
    public boolean isEmpty() {
        return count == 0;
    }

}

