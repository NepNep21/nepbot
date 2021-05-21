package me.nepnep.nepbot.database;

import com.fasterxml.jackson.core.type.TypeReference;
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
import net.dv8tion.jda.api.entities.Guild;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DefaultRoleDatabase extends Database {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultRoleDatabase.class);

    public static void setDefaultRole(Guild guild, String role) {
        long guildId = guild.getIdLong();

        try {

            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(CONNECTION_STRING)
                    .retryWrites(true)
                    .applyToSocketSettings(builder -> builder.connectTimeout(30, TimeUnit.SECONDS))
                    .build();
            MongoClient mongoClient = MongoClients.create(settings);

            MongoDatabase database = mongoClient.getDatabase("Nepbot");

            MongoCollection guilds = database.getCollection("Guilds");

            guilds.updateOne(Filters.eq("guildId", guildId),
                    Updates.combine(Updates.set("defaultRole", role)),
                    new UpdateOptions().upsert(true));

        } catch (Exception e) {
            LOGGER.warn("Exception in database", e);
        }
    }
    public static String getDefaultRole(Guild guild) {
        long guildId = guild.getIdLong();

        try {
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

            if (first != null) {
                Map<String, Object> map = new ObjectMapper().readValue(first.toJson(), new TypeReference<Map<String, Object>>() {});
                return map.get("defaultRole").toString();
            }
            return null;
        } catch (Exception e) {
            LOGGER.error("Possibly severe exception in database!", e);
            return null;
        }
    }
}
