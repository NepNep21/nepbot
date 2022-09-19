package me.nepnep.nepbot.database

import com.mongodb.client.model.Filters
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.model.Updates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.nepnep.nepbot.mongoGuilds
import net.dv8tion.jda.api.entities.Guild

suspend fun Guild.isInStarboard(message: Long): Boolean {
    val document = withContext(Dispatchers.IO) { mongoGuilds.find(Filters.eq("guildId", idLong)) }.first() ?: return false

    return message in (document.get("starboard", List::class.java) ?: listOf<Long>())
}

suspend fun Guild.addToStarboard(message: Long) {
    withContext(Dispatchers.IO) {
        mongoGuilds.updateOne(
            Filters.eq("guildId", idLong),
            Updates.addToSet("starboard", message),
            UpdateOptions().upsert(true)
        )
    }
}