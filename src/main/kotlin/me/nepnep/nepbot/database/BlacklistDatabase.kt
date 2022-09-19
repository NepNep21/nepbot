package me.nepnep.nepbot.database

import com.fasterxml.jackson.databind.ObjectMapper
import com.mongodb.client.model.Filters
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.model.Updates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.nepnep.nepbot.mongoGuilds
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel

enum class BlacklistType(val key: String) {
    LEWD("lewd"),
    TRY_IT_AND_SEE("tryItAndSee")
}

suspend fun GuildMessageChannel.addToBlacklist(type: BlacklistType) {
    withContext(Dispatchers.IO) {
        mongoGuilds.updateOne(
            Filters.eq("guildId", guild.idLong),
            Updates.addToSet("blacklist.${type.key}", idLong),
            UpdateOptions().upsert(true)
        )
    }
}

suspend fun GuildMessageChannel.isInBlacklist(type: BlacklistType): Boolean {
    val document = withContext(Dispatchers.IO) { mongoGuilds.find(Filters.eq("guildId", guild.idLong)) }.first() ?: return false

    val iterator = ObjectMapper().readTree(document.toJson())
        ?.get("blacklist")
        ?.get(type.key)
        ?.elements() ?: return false

    for (channel in iterator) {
        if (idLong == channel.longValue()) {
            return true
        }
    }
    return false
}

suspend fun GuildMessageChannel.removeFromBlacklist(type: BlacklistType) {
    withContext(Dispatchers.IO) {
        mongoGuilds.updateOne(Filters.eq("guildId", guild.idLong), Updates.pull("blacklist.${type.key}", idLong))
    }
}