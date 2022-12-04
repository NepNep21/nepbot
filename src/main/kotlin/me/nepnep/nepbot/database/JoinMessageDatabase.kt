package me.nepnep.nepbot.database

import com.mongodb.client.model.Filters
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.model.Updates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.nepnep.nepbot.mongoGuilds
import net.dv8tion.jda.api.entities.Guild
import org.bson.Document

suspend fun Guild.setJoinDetails(channel: Long, message: String) {
    val update = Updates.combine(
        Updates.set("joinMessage.channel", channel),
        Updates.set("joinMessage.message", message)
    )

    withContext(Dispatchers.IO) {
        mongoGuilds.updateOne(
            Filters.eq("guildId", idLong),
            update,
            UpdateOptions().upsert(true)
        )
    }
}

class JoinDetails(val channel: Long, val message: String)

suspend fun Guild.getJoinDetails(): JoinDetails? {
    val first = withContext(Dispatchers.IO) { 
        mongoGuilds.find(Filters.eq("guildId", idLong)).first()
    }?.getEmbedded(listOf("joinMessage"), Document::class.java) ?: return null

    return JoinDetails(
        first.get("channel", java.lang.Long::class.java).toLong(),
        first.get("message", String::class.java)
    )
}