package com.indexer;

import com.indexer.Models.TokenService;
import com.indexer.tokenizer.Token;
import com.indexer.tokenizer.Tokenizer;
import com.indexer.utils.DBManager;
import com.mongodb.client.MongoDatabase;

import java.util.HashMap;
import java.util.Set;

public class App
{
    public static void main(String[] args) {

        String docId = "13";
        String path = "/home/shehab/Desktop/DuckDive/IndexerDuck/resources/example.html";

        Tokenizer tokenizerInst = new Tokenizer(path,docId);
        tokenizerInst.tokenize();
        HashMap<String,Token> dictionary = tokenizerInst.getTokenizedData();

        MongoDatabase db = DBManager.connect("mongodb://localhost:27017/" , "SearchEngine");
        TokenService service = new TokenService(db);

        service.insertToken(dictionary,docId);


    }
}
