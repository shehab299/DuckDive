package com.crawler.Models;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class PageService {

    private final MongoCollection<Document> pageTable;
    public PageService(MongoDatabase dbConnection){
        this.pageTable = dbConnection.getCollection("page");
    }
    public void insertPage(Page p){

        Document newPage = new Document()
                .append("url", p.getUrl())
                .append("hash",p.getHash())
                .append("path",p.getPath())
                .append("is_indexed",p.getIndexed())
                .append("lastModified",p.getLastModified());

        pageTable.insertOne(newPage);
    }

}
