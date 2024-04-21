package com.indexer.Models;

import com.indexer.tokenizer.Token;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import com.mongodb.client.model.UpdateOptions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class TokenService {
    private final MongoCollection<Document> tokenTable;

    private void getToken(Token t, int docId) {

        Document query = new Document("term", t.term);

        Document find = tokenTable.find(query).first();

        if(find == null)
        {
            Document d = new Document("term" , t.term).append("documents" , new ArrayList<Document>());
            tokenTable.insertOne(d);
        }

        Document newDoc = new Document("docid", docId)
                .append("TF", t.TF)
                .append("pos", t.position);


        Document update = new Document("$addToSet", new Document("documents", newDoc));

        // If the document with the specified term exists, update it; otherwise, insert it
        tokenTable.updateOne(query, update, new UpdateOptions().upsert(true));
    }

    public TokenService(MongoDatabase dbConnection) {
        this.tokenTable = dbConnection.getCollection("InvertedIndex");
    }

    public void insertToken(HashMap<String, Token> dict, int docId){

        Set<String> keySet = dict.keySet();

        for (String key : keySet) {
            Token t = dict.get(key);
            getToken(t,docId);
        }

    }

}
