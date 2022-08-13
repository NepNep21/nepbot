package me.nepnep.nepbot.database

import com.fasterxml.jackson.databind.ObjectMapper
import com.mongodb.client.model.Filters
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.model.Updates
import me.nepnep.nepbot.mongoGuilds
import net.dv8tion.jda.api.entities.GuildMessageChannel

fun GuildMessageChannel.addToWhitelist() {
    mongoGuilds.updateOne(
        Filters.eq("guildId", guild.idLong),
        Updates.addToSet("whitelist.bottom", idLong),
        UpdateOptions().upsert(true)
    )
}

fun GuildMessageChannel.isInWhitelist(): Boolean {
    val document = mongoGuilds.find(Filters.eq("guildId", guild.idLong)).first() ?: return false

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
    mongoGuilds.updateOne(Filters.eq("guildId", guild.idLong), Updates.pull("whitelist.bottom", idLong))
}