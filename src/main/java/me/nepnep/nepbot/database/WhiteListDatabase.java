package me.nepnep.nepbot.database;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClientSettings;
import com.mongodb.ReadPreference;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class WhiteListDatabase extends Database {
    public static void addToWhiteList(/*String whitelist, when more are added*/long channel) {
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(CONNECTION_STRING)
                .retryWrites(true)
                .applyToSocketSettings(builder -> builder.connectTimeout(30, TimeUnit.SECONDS))
                .build();
        MongoClient client = MongoClients.create(settings);

        MongoCollection<Document> collection = client.getDatabase("Nepbot").getCollection("Misc");

        Document document = collection.find().first();

        collection.updateOne(document, Updates.addToSet("whitelist.bottom", channel));
        client.close();
    }
    public static boolean isInWhiteList(/*String whitelist, when more are added*/long channel) {
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(CONNECTION_STRING)
                .retryReads(true)
                .readPreference(ReadPreference.primaryPreferred())
                .applyToSocketSettings(builder -> builder.connectTimeout(30, TimeUnit.SECONDS))
                .build();
        MongoClient client = MongoClients.create(settings);

        MongoCollection<Document> collection = client.getDatabase("Nepbot").getCollection("Misc");

        Document document = collection.find().first();

        try {
            List<Long> list = Arrays.stream(new ObjectMapper()
                    .readValue(document.toJson(), DocumentObject.class)
                    .getWhitelist()
                    .get("bottom"))
                    .boxed()
                    .collect(Collectors.toList());
            client.close();
            return list.contains(channel);
        } catch (JacksonException e) {
            client.close();
            return false;
        }
    }
    public static void removeFromWhiteList(/*String whitelist, when more are added*/long channel) {
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(CONNECTION_STRING)
                .retryWrites(true)
                .applyToSocketSettings(builder -> builder.connectTimeout(30, TimeUnit.SECONDS))
                .build();
        MongoClient client = MongoClients.create(settings);

        MongoCollection<Document> collection = client.getDatabase("Nepbot").getCollection("Misc");

        Document document = collection.find().first();

        collection.updateOne(document, Updates.pull("whitelist.bottom", channel));
        client.close();
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class DocumentObject {
        Map<String, long[]> whitelist;
        public Map<String, long[]> getWhitelist() {
            return whitelist;
        }
    }
}
