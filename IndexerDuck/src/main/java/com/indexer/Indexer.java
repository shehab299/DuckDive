package com.indexer;

import com.indexer.Models.DocumentService;
import com.indexer.Models.TokenService;
import com.indexer.tokenizer.Token;
import com.indexer.tokenizer.Tokenizer;
import com.indexer.utils.DBManager;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.HashMap;
// import java.util.Objects;
// import java.util.Set;

public class Indexer
{

    private static void index(String path, int docid, TokenService service){

        Tokenizer tokenizerInst = new Tokenizer(path,docid);
        tokenizerInst.tokenize();

        HashMap<String,Token> dictionary = tokenizerInst.getTokenizedData();

        Thread thread = new Thread(new Runnable() {
            public void run() {
                service.insertToken(dictionary,docid);
            }
        });

        thread.start();
    }
    public static void main(String[] args) {

        Language.readStopWords("/home/shehab/Desktop/DuckDive/IndexerDuck/resources/stops.txt");
        MongoDatabase connection = DBManager.connect("mongodb://localhost:27017","SearchEngine");

        DocumentService docService = new DocumentService(connection);
        TokenService tokenService =  new TokenService(connection);

        FindIterable<Document> patch = docService.getUnindexedDocuments();

        long start = System.currentTimeMillis();

        int counter = 0;
        for (Document document : patch) {
            ObjectId docid = document.getObjectId("_id");
            String path = document.getString("path");
            index(path,counter,tokenService);
            docService.setIndexed(docid);
        }

        System.out.println(System.currentTimeMillis() - start);

    }
}
