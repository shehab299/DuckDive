package com.crawler.Models;

import com.crawler.Url;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import java.util.ArrayList;
import java.util.List;
import org.bson.Document;

public class PageService {

    private final MongoCollection<Document> pageTable;

    public PageService(MongoDatabase dbConnection) {
        this.pageTable = dbConnection.getCollection("page");
    }

    public void insertPage(Page p) {

        Document newPage = new Document()
                .append("url", p.getUrl())
                .append("originalUrl", p.getOriginalUrl())
                .append("hash", p.getHash())
                .append("path", p.getPath())
                .append("outlinks", p.getOutlinks())
                .append("is_indexed", p.getIndexed())
                .append("is_ranked", p.getRanked());

        pageTable.insertOne(newPage);
    }

    public List<String> getOutlinks(Url[] extractedURLs) {
        try {
            List<String> outlinks = new ArrayList<>();
            for (Url url : extractedURLs) {
                String normalized = url.getNormalized();
                outlinks.add(normalized);
            }
            return outlinks;
        } catch (Exception e) {
            System.err.println("Crawler: Error fetching outlinks: " + e.getMessage());
            return null;
        }
    }

    public long countUrls() {
        return pageTable.countDocuments();
    }

    public boolean hashExists(String hash) {
        Document query = new Document("hash", hash);
        Document result = pageTable.find(query).first();

        return result != null;
    }

    public boolean urlExists(Url url) {
        Document query = new Document("url", url.getNormalized());
        Document result = pageTable.find(query).first();

        return result != null;
    }

    public Page getPage(Url url) {
        Document query = new Document("url", url.getNormalized());
        Document result = pageTable.find(query).first();

        if (result == null) {
            return null;
        }

        return new Page(result);
    }

}