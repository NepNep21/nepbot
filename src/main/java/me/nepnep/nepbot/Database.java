package me.nepnep.nepbot;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ReadPreference;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import net.dv8tion.jda.api.entities.Guild;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

public class Database {

    private static final String MONGODB_URL = System.getenv("MONGODB_URL");
    private static final Logger LOGGER = LoggerFactory.getLogger(Database.class);

    public static void write(Guild guild, String role) {
        long guildId = guild.getIdLong();

        try {
        ConnectionString connectionString = new ConnectionString(MONGODB_URL);

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .retryWrites(true)
                .applyToSocketSettings(builder -> builder.connectTimeout(30, TimeUnit.SECONDS))
                .build();
        MongoClient mongoClient = MongoClients.create(settings);

            MongoDatabase database = mongoClient.getDatabase("DefaultRoles");

            MongoCollection roles = database.getCollection("Roles");

            // IndexOptions options = new IndexOptions().unique(true);

            // roles.createIndex(Indexes.ascending(String.valueOf(guildId)), options);

            // Document guildDoc = new Document(String.valueOf(guildId), role);

            // roles.insertOne(guildDoc);

            // roles.updateOne(new Document("_id", "600f521f221589346dcb4ddf"), new Document(String.valueOf(guildId), role));

            // roles.findOneAndUpdate(new Document("flag", true), new Document(String.valueOf(guildId), role));

            roles.updateOne(Filters.eq("_id", new ObjectId("600f521f221589346dcb4ddf")), combine(set(String.valueOf(guildId), role)));

        } catch (Exception e) {
            LOGGER.warn("Exception in database", e);
        }
    }
    public static String read(Guild guild) {
        long guildId = guild.getIdLong();

        try {
            ConnectionString connectionString = new ConnectionString(MONGODB_URL);

            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(connectionString)
                    .retryReads(true)
                    .readPreference(ReadPreference.primaryPreferred())
                    .applyToSocketSettings(builder -> builder.connectTimeout(30, TimeUnit.SECONDS))
                    .build();
            MongoClient mongoClient = MongoClients.create(settings);

            MongoDatabase database = mongoClient.getDatabase("DefaultRoles");

            MongoCollection roles = database.getCollection("Roles");

            String document = roles.find()
                    .projection(new Document(String.valueOf(guildId), 1)
                    .append("_id", 0)).first().toString();
            String role = document.replaceAll("Document", "").replaceAll(String.valueOf(guildId), "").substring(3).replaceAll("}", "");

            return role;
        } catch (Exception e) {
            LOGGER.error("Possibly severe exception in database!", e);
            return null;
        }
    }
}
