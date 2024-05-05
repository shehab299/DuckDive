package com.page_rank.Models;

import com.mongodb.MongoException;
import com.mongodb.client.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import javax.print.Doc;

public class PageService {
    private final MongoCollection<Document> documentTable;

    public PageService(MongoDatabase dbConnection) {
        this.documentTable = dbConnection.getCollection("page");
    }

    public FindIterable<Document> getUnRankedDocuments() {

        Document projection = new Document("_id", true).append("path", true);
        Document query = new Document("is_ranked", false);

        return documentTable.find(query).limit(499).projection(projection);
    }

    public List<Document> fetchAllDocuments(){

        ArrayList<Document> distinctDocs = null;

        try {
            Document projection = new Document("url", true).append("outlinks",true);
            Document query = new Document("is_ranked", false);

            distinctDocs = documentTable.find(query).projection(projection).into(new ArrayList<Document>());
            
        } catch (MongoException e) {
            System.out.println("Error fetching distinct documents: " + e.getMessage());
        }

        return distinctDocs;

    }
}
