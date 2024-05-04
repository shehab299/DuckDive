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

import java.nio.file.Paths;
import java.util.HashMap;


public class Indexer
{
    private static TokenService tokenService;
    private static DocumentService docService;
    private static String stopsPath;
    public static String docPath;
    public static String scorePath;


    private static void initailizePath(){
        String currentDirectory = System.getProperty("user.dir");
        stopsPath = Paths.get(currentDirectory, "resources", "stops.txt").toString();
        docPath = Paths.get(currentDirectory, "plain_docs", " ").toString();
        scorePath = Paths.get(currentDirectory, "resources" , "html.txt").toString();
        docPath = docPath.trim();
    }

    private static void initailizeDB()
    {
        MongoDatabase connection = DBManager.connect("mongodb://localhost:27017","SearchEngine");
        docService = new DocumentService(connection);
        tokenService =  new TokenService(connection);
    }

    private static void index(String path, ObjectId docid, String doc_path){
        Tokenizer tokenizerInst = new Tokenizer(path);
        HashMap<String,Token> dictionary = tokenizerInst.tokenizeDocument(doc_path);
        tokenService.insert(dictionary,docid.toString(),tokenizerInst.getWordCount());
    }


    public static void main(String[] args) {

        initailizePath();
        initailizeDB();

        Language.initalizeDictionary(stopsPath);
        Language.initalizeHTML(scorePath);


        FindIterable<Document> patch = docService.getUnindexedDocuments();

        for (Document document : patch) {
            ObjectId docid = document.getObjectId("_id");
            String path = document.getString("path");
            String doc_path = docPath + docid + ".txt";
            index(path,docid,doc_path);
            docService.setIndexed(docid,doc_path);
        }
    }
}
