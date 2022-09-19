package me.nepnep.nepbot.database

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.mongodb.client.model.Filters
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.model.Updates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.nepnep.nepbot.mongoGuilds
import net.dv8tion.jda.api.entities.Guild

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

suspend fun Guild.getJoinDetails(): JsonNode? {
    val first = withContext(Dispatchers.IO) { mongoGuilds.find(Filters.eq("guildId", idLong)) }.first()

    first ?: return null

    return ObjectMapper().readTree(first.toJson())["joinMessage"]
}