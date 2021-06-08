package me.nepnep.nepbot.database;

import com.mongodb.MongoClientSettings;
import com.mongodb.ReadPreference;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class StarboardDatabase extends Database {
    public static boolean hasMessage(long message) {
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(CONNECTION_STRING)
                .retryReads(true)
                .readPreference(ReadPreference.primaryPreferred())
                .applyToSocketSettings(builder -> builder.connectTimeout(30, TimeUnit.SECONDS))
                .build();
        MongoClient mongoClient = MongoClients.create(settings);

        MongoDatabase database = mongoClient.getDatabase("Nepbot");

        MongoCollection<Document> collection = database.getCollection("Misc");

        Document document = collection.find().first();

        List<Long> messages = document.getList("starboard", Long.class);

        mongoClient.close();
        return messages.contains(message);
    }
    public static void addMessage(long message) {
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(CONNECTION_STRING)
                .retryWrites(true)
                .applyToSocketSettings(builder -> builder.connectTimeout(30, TimeUnit.SECONDS))
                .build();
        MongoClient client = MongoClients.create(settings);

        MongoCollection<Document> collection = client.getDatabase("Nepbot").getCollection("Misc");

        Document document = collection.find().first();

        collection.findOneAndUpdate(document, Updates.addToSet("starboard", message));
        client.close();
    }
}
