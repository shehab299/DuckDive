package com.crawler;


import com.crawler.Models.Page;
import com.crawler.Models.PageService;
import com.crawler.utils.DBManager;
import com.mongodb.client.*;



public class App
{
    public static void main( String[] args )
    {
        String connString = "mongodb+srv://shehab:shahab1234@tasks.e3rqvm7.mongodb.net/?retryWrites=true&w=majority";
        MongoDatabase connection = DBManager.connect(connString,"Tasks");

        PageService pageDb = new PageService(connection);

        Page x = new Page("shehab", "shehab", "shehab", true, "shehab");
        pageDb.insertPage(x);

        DBManager.disconnect();

    }
}
