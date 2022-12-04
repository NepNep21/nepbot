package me.nepnep.nepbot.database

import com.mongodb.client.model.Filters
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.model.Updates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.nepnep.nepbot.mongoGuilds
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel
import org.bson.Document

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
    val iterator = withContext(Dispatchers.IO) { 
        mongoGuilds.find(Filters.eq("guildId", guild.idLong)).first()
    }?.getEmbedded(listOf("whitelist"), Document::class.java)
        ?.getList("bottom", java.lang.Long::class.java) ?: return false

    return iterator.any { it.toLong() == idLong } // Can't use contains() aaaaa
}

suspend fun GuildMessageChannel.removeFromWhitelist() {
    withContext(Dispatchers.IO) {
        mongoGuilds.updateOne(Filters.eq("guildId", guild.idLong), Updates.pull("whitelist.bottom", idLong))
    }
}