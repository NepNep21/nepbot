package me.nepnep.nepbot.database

import com.mongodb.client.model.Filters
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.model.Updates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.nepnep.nepbot.mongoGuilds
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Role

suspend fun Guild.setDefaultRole(role: Role?) {
    val filter = Filters.eq("guildId", idLong)

    withContext(Dispatchers.IO) {
        if (role == null) {
            mongoGuilds.updateOne(
                filter,
                Updates.unset("defaultRole")
            )
            return@withContext 
        }

        mongoGuilds.updateOne(
            filter,
            Updates.set("defaultRole", role.name),
            UpdateOptions().upsert(true)
        )
    }
}

suspend fun Guild.getDefaultRole(): Role? {
    val name = withContext(Dispatchers.IO) { mongoGuilds.find(Filters.eq("guildId", idLong)) }
        .first()
        ?.get("defaultRole", String::class.java) ?: return null

    val roles = getRolesByName(name, false)
    if (roles.isEmpty()) {
        return null
    }

    return roles[0]
}