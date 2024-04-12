package com.crawler;

import java.util.LinkedList;
import java.util.Queue;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Frontier {
    private Queue<String> urlQueue;
    private int count;

    public Frontier()
    {
        urlQueue=new LinkedList<String>();
        count=0;
    }

    public void readSeed(String seedPath)
    {
        try (BufferedReader reader = new BufferedReader(new FileReader(seedPath)))
        {
            String url;
            while ((url = reader.readLine()) != null)
                addurl(url);
        } 
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void addurl(String url)
    {
        urlQueue.offer(url);
        count++;
    }

    public String getNexturl()
    {
        if(count > 0)
            count--;
        return urlQueue.poll();
    }
//Test Function
    public static void main(String[] argv)
    {
        String seedPath="E:\\Education\\CMP_SecYear\\SecondSemester\\APT\\WebCrawler\\Frontier\\src\\seed.txt";
        Frontier frontier=new Frontier();
        frontier.readSeed(seedPath);
        frontier.addurl("Ay7aga.com");
        String url=frontier.getNexturl();
        while(url != null)
        {
            System.out.println(url);
            url=frontier.getNexturl();
        }
    }
}
