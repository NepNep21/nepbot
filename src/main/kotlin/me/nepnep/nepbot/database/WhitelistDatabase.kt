package me.nepnep.nepbot.database

import com.fasterxml.jackson.databind.ObjectMapper
import com.mongodb.client.model.Filters
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.model.Updates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.nepnep.nepbot.mongoGuilds
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel

suspend fun GuildMessageChannel.addToWhitelist() {
    withContext(Dispatchers.IO) {
        mongoGuilds.updateOne(
            Filters.eq("guildId", guild.idLong),
            Updates.addToSet("whitelist.bottom", idLong),
            UpdateOptions().upsert(true)
        )
    }
}

suspend fun GuildMessageChannel.isInWhitelist(): Boolean {
    val document = withContext(Dispatchers.IO) { mongoGuilds.find(Filters.eq("guildId", guild.idLong)) }.first() ?: return false

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

suspend fun GuildMessageChannel.removeFromWhitelist() {
    withContext(Dispatchers.IO) {
        mongoGuilds.updateOne(Filters.eq("guildId", guild.idLong), Updates.pull("whitelist.bottom", idLong))
    }
}