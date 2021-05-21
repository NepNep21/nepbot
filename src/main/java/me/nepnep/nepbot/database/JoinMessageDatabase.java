package me.nepnep.nepbot.database;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClientSettings;
import com.mongodb.ReadPreference;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class JoinMessageDatabase extends Database {
    public static void setJoinDetails(String channel, String message, long guildId) {
        long channelLong = Long.parseLong(channel);
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(CONNECTION_STRING)
                .retryWrites(true)
                .applyToSocketSettings(builder -> builder.connectTimeout(30, TimeUnit.SECONDS))
                .build();
        MongoClient client = MongoClients.create(settings);

        MongoCollection guilds = client.getDatabase("Nepbot").getCollection("Guilds");

        guilds.updateOne(Filters.eq("guildId", guildId),
                Updates.combine(Updates.set("joinMessage.channel", channelLong)),
                new UpdateOptions().upsert(true));
        guilds.updateOne(Filters.eq("guildId", guildId),
                Updates.combine(Updates.set("joinMessage.message", message)),
                new UpdateOptions().upsert(true));
    }
    public static Map<String, String> getJoinDetails(long guildId) {
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(CONNECTION_STRING)
                .retryReads(true)
                .readPreference(ReadPreference.primaryPreferred())
                .applyToSocketSettings(builder -> builder.connectTimeout(30, TimeUnit.SECONDS))
                .build();
        MongoClient mongoClient = MongoClients.create(settings);

        MongoDatabase database = mongoClient.getDatabase("Nepbot");

        MongoCollection<Document> guilds = database.getCollection("Guilds");

        Document first = guilds
                .find(new BasicDBObject("guildId", guildId))
                .first();
        if (first != null && first.containsKey("joinMessage")) {
            try {
                DocumentObject object = new ObjectMapper().readValue(first.toJson(), DocumentObject.class);
                JoinMessageObject joinMessage = object.getJoinMessage();

                Map<String, String> details = new HashMap<>();
                details.put("channel", String.valueOf(joinMessage.getChannel()));
                details.put("message", joinMessage.getMessage());
                return details;
            } catch (JacksonException e) {
                // Intentionally empty
            }
        }
        return null;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class DocumentObject {
        JoinMessageObject joinMessage;

        public JoinMessageObject getJoinMessage() {
            return joinMessage;
        }
    }
    private static class JoinMessageObject {
        long channel;
        String message;

        public long getChannel() {
            return channel;
        }
        public String getMessage() {
            return message;
        }
    }
}
