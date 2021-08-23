package me.nepnep.nepbot.database

import com.fasterxml.jackson.databind.ObjectMapper
import com.mongodb.client.MongoClients
import com.mongodb.client.model.Filters
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.model.Updates
import me.nepnep.nepbot.DB_NAME
import me.nepnep.nepbot.mongoConnectionString
import net.dv8tion.jda.api.entities.TextChannel

fun TextChannel.addToBlacklist() {
    val client = MongoClients.create(mongoConnectionString)
    val collection = client.getDatabase(DB_NAME).getCollection("Guilds")

    collection.updateOne(
        Filters.eq("guildId", guild.idLong),
        Updates.addToSet("blacklist.lewd", idLong),
        UpdateOptions().upsert(true)
    )
    client.close()
}

fun TextChannel.isInBlacklist(): Boolean {
    val client = MongoClients.create(mongoConnectionString)
    val collection = client.getDatabase(DB_NAME).getCollection("Guilds")

    val document = collection.find(Filters.eq("guildId", guild.idLong)).first() ?: return false

    val iterator = ObjectMapper().readTree(document.toJson())
        ?.get("blacklist")
        ?.get("lewd")
        ?.elements() ?: return false

    for (channel in iterator) {
        if (idLong == channel.longValue()) {
            client.close()
            return true
        }
    }
    client.close()
    return false
}

fun TextChannel.removeFromBlacklist() {
    val client = MongoClients.create(mongoConnectionString)
    val collection = client.getDatabase(DB_NAME).getCollection("Guilds")

    collection.updateOne(Filters.eq("guildId", guild.idLong), Updates.pull("blacklist.lewd", idLong))
    client.close()
}