package me.nepnep.nepbot.database

import com.fasterxml.jackson.databind.ObjectMapper
import com.mongodb.BasicDBObject
import com.mongodb.client.MongoClients
import com.mongodb.client.model.Filters
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.model.Updates
import me.nepnep.nepbot.DB_NAME
import me.nepnep.nepbot.mongoConnectionString
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Role

fun Guild.setDefaultRole(role: Role?) {
    val client = MongoClients.create(mongoConnectionString)
    val guilds = client.getDatabase(DB_NAME).getCollection("Guilds")

    val filter = Filters.eq("guildId", idLong)

    if (role == null) {
        guilds.updateOne(
            filter,
            Updates.unset("defaultRole")
        )
        client.close()
        return
    }

    guilds.updateOne(
        filter,
        Updates.combine(Updates.set("defaultRole", role.name)),
        UpdateOptions().upsert(true)
    )
    client.close()
}

fun Guild.getDefaultRole(): Role? {
    val client = MongoClients.create(mongoConnectionString)
    val guilds = client.getDatabase(DB_NAME).getCollection("Guilds")

    val first = guilds.find(BasicDBObject("guildId", idLong)).first()

    client.close()
    first ?: return null
    val roleNode = ObjectMapper().readTree(first.toJson())["defaultRole"]

    // May not be required, exists to be safe, more testing is needed
    roleNode ?: return null

    val name = roleNode.textValue()

    val roles = getRolesByName(name, false)
    if (roles.isEmpty()) {
        return null
    }

    return roles[0]
}