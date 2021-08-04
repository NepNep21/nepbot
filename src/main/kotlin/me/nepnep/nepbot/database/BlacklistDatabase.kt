package me.nepnep.nepbot.database

import com.fasterxml.jackson.databind.ObjectMapper
import com.mongodb.client.MongoClients
import com.mongodb.client.model.Updates
import me.nepnep.nepbot.DB_NAME
import me.nepnep.nepbot.mongoConnectionString
import net.dv8tion.jda.api.entities.TextChannel

fun TextChannel.addToBlacklist() {
    val client = MongoClients.create(mongoConnectionString)
    val collection = client.getDatabase(DB_NAME).getCollection("Misc")
    val document = collection.find().first()!!

    collection.updateOne(document, Updates.addToSet("blacklist.lewd", idLong))
    client.close()
}

fun TextChannel.isInBlacklist(): Boolean {
    val client = MongoClients.create(mongoConnectionString)
    val collection = client.getDatabase(DB_NAME).getCollection("Misc")
    val document = collection.find().first()!!

    val iterator = ObjectMapper().readTree(document.toJson())["blacklist"]["lewd"].elements()

    for (channel in iterator) {
        if (idLong == channel.longValue()) {
            client.close()
            return true
        }
    }
    client.close()
    return false
}

fun TextChannel.removeFromBlacklist() {
    val client = MongoClients.create(mongoConnectionString)
    val collection = client.getDatabase(DB_NAME).getCollection("Misc")
    val document = collection.find().first()!!

    collection.updateOne(document, Updates.pull("blacklist.lewd", idLong))
    client.close()
}