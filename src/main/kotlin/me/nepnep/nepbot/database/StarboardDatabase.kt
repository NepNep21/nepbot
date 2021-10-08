package me.nepnep.nepbot.database

import com.mongodb.client.model.Filters
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.model.Updates
import me.nepnep.nepbot.DB_NAME
import me.nepnep.nepbot.mongoClient
import net.dv8tion.jda.api.entities.Guild

fun Guild.isInStarboard(message: Long): Boolean {
    val collection = mongoClient.getDatabase(DB_NAME).getCollection("Guilds")
    val document = collection.find(Filters.eq("guildId", idLong)).first() ?: return false

    return message in (document.get("starboard", List::class.java) ?: listOf<Long>())
}

fun Guild.addToStarboard(message: Long) {
    val collection = mongoClient.getDatabase(DB_NAME).getCollection("Guilds")

    collection.updateOne(Filters.eq("guildId", idLong),
        Updates.addToSet("starboard", message),
        UpdateOptions().upsert(true)
    )
}