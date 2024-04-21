package com.indexer.Models;

// import com.indexer.tokenizer.Doc;
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

        return documentTable.find(query).limit(499).projection(projection);
    }

    public Document getDummy(){
        Document projection = new Document("_id",true).append("path",true);
        ObjectId x = new ObjectId("6624325457fa294b09dea102");
        Document query = new Document("_id",x);

        return documentTable.find(query).first();
    }

    public void setIndexed(ObjectId id){

        Document query = new Document("_id",id);
        Document update = new Document("$set", new Document("is_indexed",true));

        documentTable.updateOne(query,update);
    }

}
