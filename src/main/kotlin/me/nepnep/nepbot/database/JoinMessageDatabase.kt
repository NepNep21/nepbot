package me.nepnep.nepbot.database

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.mongodb.BasicDBObject
import com.mongodb.client.model.Filters
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.model.Updates
import me.nepnep.nepbot.DB_NAME
import me.nepnep.nepbot.mongoClient
import net.dv8tion.jda.api.entities.Guild

fun Guild.setJoinDetails(channel: Long, message: String) {
    val guilds = mongoClient.getDatabase(DB_NAME).getCollection("Guilds")
    val update = Updates.combine(
        Updates.set("joinMessage.channel", channel),
        Updates.set("joinMessage.message", message)
    )

    guilds.updateOne(
        Filters.eq("guildId", idLong),
        update,
        UpdateOptions().upsert(true)
    )
}

fun Guild.getJoinDetails(): JsonNode? {
    val guilds = mongoClient.getDatabase(DB_NAME).getCollection("Guilds")

    val first = guilds.find(BasicDBObject("guildId", idLong)).first()

    first ?: return null

    return ObjectMapper().readTree(first.toJson())["joinMessage"]
}