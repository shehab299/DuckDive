package com.indexer.Models;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

public class DocumentService {

    private final MongoCollection<Document> documentTable;

    public DocumentService(MongoDatabase connection)
    {
        this.documentTable = connection.getCollection("page");
    }

    

    public FindIterable<Document> getUnindexedDocuments(){

        Document projection = new Document("_id",true).append("path",true);
        Document query = new Document("is_indexed",false);

        return documentTable.find(query).projection(projection);
    }

    public void setIndexed(ObjectId id, String docPath){

        Document query = new Document("_id",id);
        Document updates = new Document("is_indexed",true).append("doc_path",docPath);
        Document updateQuery = new Document("$set", updates);

        documentTable.updateOne(query,updateQuery);
    }



}
