package me.nepnep.nepbot.database

import com.mongodb.client.model.Filters
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.model.Updates
import me.nepnep.nepbot.DB_NAME
import me.nepnep.nepbot.DEFAULT_PREFIX
import me.nepnep.nepbot.mongoClient
import net.dv8tion.jda.api.entities.Guild

fun Guild.getPrefix(): String {
    val guild = mongoClient.getDatabase(DB_NAME)
        .getCollection("Guilds")
        .find(Filters.eq("guildId", idLong))
        .first()
    return guild?.get("prefix", String::class.java) ?: DEFAULT_PREFIX
}

fun Guild.setPrefix(prefix: String?) {
    val guilds = mongoClient.getDatabase(DB_NAME).getCollection("Guilds")
    val filter = Filters.eq("guildId", idLong)
    if (prefix == null) {
        guilds.findOneAndUpdate(filter, Updates.unset("prefix"))
        return
    }
    guilds.updateOne(filter, Updates.set("prefix", prefix), UpdateOptions().upsert(true))
}