package me.nepnep.nepbot.database

import com.fasterxml.jackson.databind.ObjectMapper
import com.mongodb.client.model.Filters
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.model.Updates
import me.nepnep.nepbot.DB_NAME
import me.nepnep.nepbot.mongoClient
import net.dv8tion.jda.api.entities.GuildMessageChannel

fun GuildMessageChannel.addToWhitelist() {
    val collection = mongoClient.getDatabase(DB_NAME).getCollection("Guilds")

    collection.updateOne(
        Filters.eq("guildId", guild.idLong),
        Updates.addToSet("whitelist.bottom", idLong),
        UpdateOptions().upsert(true)
    )
}

fun GuildMessageChannel.isInWhitelist(): Boolean {
    val collection = mongoClient.getDatabase(DB_NAME).getCollection("Guilds")
    val document = collection.find(Filters.eq("guildId", guild.idLong)).first() ?: return false

    val iterator = ObjectMapper().readTree(document.toJson())
        ?.get("whitelist")
        ?.get("bottom")
        ?.elements() ?: return false

    for (channel in iterator) {
        if (idLong == channel.longValue()) {
            return true
        }
    }
    return false
}

fun GuildMessageChannel.removeFromWhitelist() {
    val collection = mongoClient.getDatabase(DB_NAME).getCollection("Guilds")

    collection.updateOne(Filters.eq("guildId", guild.idLong), Updates.pull("whitelist.bottom", idLong))
}