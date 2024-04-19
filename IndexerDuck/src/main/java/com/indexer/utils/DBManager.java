package com.indexer.utils;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;


import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class DBManager {

    private static MongoDatabase db;
    private static MongoClient client;

    public static MongoDatabase connect(String conString, String dbname)
    {
        if(client == null)
        {

            ServerApi serverApi = ServerApi.builder()
                    .version(ServerApiVersion.V1)
                    .build();

            CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                    fromProviders(PojoCodecProvider.builder().automatic(true).build()));


            MongoClientSettings settings = MongoClientSettings.builder()
                    .codecRegistry(pojoCodecRegistry)
                    .applyConnectionString(new ConnectionString(conString))
                    .serverApi(serverApi)
                    .build();


            client = MongoClients.create(settings);

            try {
                db = client.getDatabase(dbname);
                db.runCommand(new Document("ping", 1));
                System.out.println("successfully connected to MongoDB!");
            } catch (MongoException e) {
                e.printStackTrace();
            }
        }

        return db;
    }

    public static void disconnect() {
        if (db != null) {
            client.close();
            client = null;
            db = null;
            System.out.println("Disconnected from MongoDB.");
        }
    }
}


