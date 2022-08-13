package me.nepnep.nepbot.database

import com.mongodb.client.model.Filters
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.model.Updates
import me.nepnep.nepbot.mongoGuilds
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Role

fun Guild.setDefaultRole(role: Role?) {
    val filter = Filters.eq("guildId", idLong)

    if (role == null) {
        mongoGuilds.updateOne(
            filter,
            Updates.unset("defaultRole")
        )
        return
    }

    mongoGuilds.updateOne(
        filter,
        Updates.set("defaultRole", role.name),
        UpdateOptions().upsert(true)
    )
}

fun Guild.getDefaultRole(): Role? {
    val name = mongoGuilds.find(Filters.eq("guildId", idLong))
        .first()
        ?.get("defaultRole", String::class.java) ?: return null

    val roles = getRolesByName(name, false)
    if (roles.isEmpty()) {
        return null
    }

    return roles[0]
}