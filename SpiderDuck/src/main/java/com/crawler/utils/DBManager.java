package com.crawler.utils;

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

    private MongoDatabase db;
    private MongoClient client;

    public DBManager()
    {
        this.client = null;
        this.db = null;
    }

    public MongoDatabase connect(String conString,String dbname)
    {
        if(this.client == null)
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


            this.client = MongoClients.create(settings);

            try {
                this.db = this.client.getDatabase(dbname);
                this.db.runCommand(new Document("ping", 1));
                System.out.println("successfully connected to MongoDB!");
            } catch (MongoException e) {
                e.printStackTrace();
            }
        }

        return db;
    }

    public void disconnect() {
        if (this.db != null) {
            this.client.close();
            this.client = null;
            this.db = null;
            System.out.println("Disconnected from MongoDB.");
        }
    }
}


