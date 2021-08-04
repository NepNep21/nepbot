package me.nepnep.nepbot.database

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.mongodb.BasicDBObject
import com.mongodb.client.MongoClients
import com.mongodb.client.model.Filters
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.model.Updates
import me.nepnep.nepbot.DB_NAME
import me.nepnep.nepbot.mongoConnectionString
import net.dv8tion.jda.api.entities.Guild

fun Guild.setJoinDetails(channel: Long, message: String) {
    val client = MongoClients.create(mongoConnectionString)
    val guilds = client.getDatabase(DB_NAME).getCollection("Guilds")
    val update = Updates.combine(
        Updates.set("joinMessage.channel", channel),
        Updates.set("joinMessage.message", message)
    )

    guilds.updateOne(
        Filters.eq("guildId", idLong),
        update,
        UpdateOptions().upsert(true)
    )

    client.close()
}

fun Guild.getJoinDetails(): JsonNode? {
    val client = MongoClients.create(mongoConnectionString)
    val guilds = client.getDatabase(DB_NAME).getCollection("Guilds")

    val first = guilds.find(BasicDBObject("guildId", idLong)).first()

    client.close()
    first ?: return null

    return ObjectMapper().readTree(first.toJson())["joinMessage"]
}