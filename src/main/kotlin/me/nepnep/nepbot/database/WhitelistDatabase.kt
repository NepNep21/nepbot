package me.nepnep.nepbot.database

import com.fasterxml.jackson.databind.ObjectMapper
import com.mongodb.client.MongoClients
import com.mongodb.client.model.Updates
import me.nepnep.nepbot.DB_NAME
import me.nepnep.nepbot.mongoConnectionString
import net.dv8tion.jda.api.entities.TextChannel

fun TextChannel.addToWhitelist() {
    val client = MongoClients.create(mongoConnectionString)
    val collection = client.getDatabase(DB_NAME).getCollection("Misc")
    val document = collection.find().first()!!

    collection.updateOne(document, Updates.addToSet("whitelist.bottom", idLong))
    client.close()
}

fun TextChannel.isInWhitelist(): Boolean {
    val client = MongoClients.create(mongoConnectionString)
    val collection = client.getDatabase(DB_NAME).getCollection("Misc")
    val document = collection.find().first()!!

    val iterator = ObjectMapper().readTree(document.toJson())["whitelist"]["bottom"].elements()

    for (channel in iterator) {
        if (idLong == channel.longValue()) {
            client.close()
            return true
        }
    }
    client.close()
    return false
}

fun TextChannel.removeFromWhitelist() {
    val client = MongoClients.create(mongoConnectionString)
    val collection = client.getDatabase(DB_NAME).getCollection("Misc")
    val document = collection.find().first()!!

    collection.updateOne(document, Updates.pull("whitelist.bottom", idLong))
    client.close()
}