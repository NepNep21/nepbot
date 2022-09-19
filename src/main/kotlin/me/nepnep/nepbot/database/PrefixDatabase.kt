package me.nepnep.nepbot.database

import com.mongodb.client.model.Filters
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.model.Updates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.nepnep.nepbot.DEFAULT_PREFIX
import me.nepnep.nepbot.mongoGuilds
import net.dv8tion.jda.api.entities.Guild

suspend fun Guild.getPrefix(): String {
    val guild = withContext(Dispatchers.IO) { mongoGuilds.find(Filters.eq("guildId", idLong)) }.first()
    return guild?.get("prefix", String::class.java) ?: DEFAULT_PREFIX
}

suspend fun Guild.setPrefix(prefix: String?) {
    val filter = Filters.eq("guildId", idLong)
    withContext(Dispatchers.IO) {
        if (prefix == null) {
            mongoGuilds.updateOne(filter, Updates.unset("prefix"))
            return@withContext 
        }
        mongoGuilds.updateOne(filter, Updates.set("prefix", prefix), UpdateOptions().upsert(true))
    }
}