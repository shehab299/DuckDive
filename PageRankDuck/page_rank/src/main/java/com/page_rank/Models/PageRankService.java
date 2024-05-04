package com.page_rank.Models;

import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.MongoCursor;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

public class PageRankService {
    private final MongoCollection<Document> documentTable;

    public PageRankService(MongoDatabase dbConnection) {
        this.documentTable = dbConnection.getCollection("page");
    }

    public FindIterable<Document> getUnRankedDocuments() {

        Document projection = new Document("_id", true).append("path", true);
        Document query = new Document("is_ranked", false);

        return documentTable.find(query).limit(499).projection(projection);
    }

    public List<Document> fetchAllDocuments() {
        System.out.println("Fetching documents now");
        List<Document> documents = new ArrayList<>();

        try (MongoCursor<Document> cursor = documentTable.find().iterator()) {
            while (cursor.hasNext()) {
                documents.add(cursor.next());
            }
        } catch (MongoException e) {
            System.out.println("Error fetching documents: " + e.getMessage());
        }

        return documents;
    }
}
