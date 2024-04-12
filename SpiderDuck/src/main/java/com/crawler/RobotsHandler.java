package com.crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class RobotsHandler {
    private static final String USER_AGENT = "DuckDive"; 
    public static boolean canBeCrawled(Url url)
    {
        try
        {
            HttpURLConnection connection = (HttpURLConnection) new URL(url.getRobots()).openConnection();
            connection.setRequestProperty("User-Agent", USER_AGENT);
            BufferedReader reader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            boolean foundAgent=false;
            while((line = reader.readLine()) != null)
            {
                if(line.toLowerCase().startsWith("user-agent")) 
                    if(line.endsWith("*") || line.endsWith(USER_AGENT))
                        foundAgent=true;

                if(foundAgent)
                    while ((line=reader.readLine()) != null && foundAgent == true)
                    {
                        if(line.toLowerCase().startsWith("user-agent"))
                            foundAgent=false; //a break would do bc it doesn't matter in our case but i think this is the right thing to do
                        else if(line.toLowerCase().startsWith("disallow:"))
                        {
                            String path=line.substring("disallow: ".length());
                            if(url.getNormalized().contains(path)) //disallowed
                            {
                                while ((line = reader.readLine()) != null && line.toLowerCase().startsWith("allow:")) //making sure that the path is not in the allowed section
                                {   //this is a rare condition but mut be handled 🥲
                                    String exactPath=url.getBase() + line.substring("allow: ".length());
                                    if((url.getNormalized()).equals(exactPath))
                                        return true;
                                }
                                return false;
                            }  
                            
                        }
                    }           
            }
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    // public static void main(String[] argv)
    // {
    //     //refer to github.com/robots.txt to test this class
    //     String Aurl = "https://github.com/*?tab=achievements&achievement=*"; //in the allowed section
    //     String Iurl = "https://github.com/shehab299/DuckDive"; //not in the disallowed section
    //     String Durl = "https://github.com/*/tree/"; //disallowed section

    //     Url AurlObj = new Url(Aurl);
    //     Url DurlObj = new Url(Durl);
    //     Url IurlObj = new Url(Iurl);
    //     System.out.println(canBeCrawled(AurlObj)+"\n");
    //     System.out.println(canBeCrawled(IurlObj)+"\n");
    //     System.out.println(canBeCrawled(DurlObj)+"\n");
    // }
}
