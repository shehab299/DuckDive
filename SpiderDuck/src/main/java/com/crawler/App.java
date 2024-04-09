package com.crawler;


import com.crawler.utils.DBManager;
import com.mongodb.client.*;



public class App
{
    public static void main( String[] args )
    {
        String connString = "mongodb+srv://shehab:shahab1234@tasks.e3rqvm7.mongodb.net/?retryWrites=true&w=majority";
        DBManager db = new DBManager();
        MongoDatabase database = db.connect(connString,"Tasks");

        Page p = new Page("SHEHAB","SHEHAB","SEHAB",true,"SHEHAB");
        System.out.println(p.getHash());

        db.disconnect();

    }
}
