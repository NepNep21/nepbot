package me.nepnep.nepbot.database

import com.mongodb.client.model.Filters
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.model.Updates
import me.nepnep.nepbot.DB_NAME
import me.nepnep.nepbot.mongoClient
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Role

fun Guild.setDefaultRole(role: Role?) {
    val guilds = mongoClient.getDatabase(DB_NAME).getCollection("Guilds")

    val filter = Filters.eq("guildId", idLong)

    if (role == null) {
        guilds.updateOne(
            filter,
            Updates.unset("defaultRole")
        )
        return
    }

    guilds.updateOne(
        filter,
        Updates.set("defaultRole", role.name),
        UpdateOptions().upsert(true)
    )
}

fun Guild.getDefaultRole(): Role? {
    val guilds = mongoClient.getDatabase(DB_NAME).getCollection("Guilds")

    val name = guilds.find(Filters.eq("guildId", idLong))
        .first()
        ?.get("defaultRole", String::class.java) ?: return null

    val roles = getRolesByName(name, false)
    if (roles.isEmpty()) {
        return null
    }

    return roles[0]
}