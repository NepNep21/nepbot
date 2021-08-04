package me.nepnep.nepbot.database

import com.fasterxml.jackson.databind.ObjectMapper
import com.mongodb.client.MongoClients
import com.mongodb.client.model.Updates
import me.nepnep.nepbot.DB_NAME
import me.nepnep.nepbot.mongoConnectionString

fun isInStarboard(message: Long): Boolean {
    val client = MongoClients.create(mongoConnectionString)
    val collection = client.getDatabase(DB_NAME).getCollection("Misc")
    val document = collection.find().first()!!

    val iterator = ObjectMapper().readTree(document.toJson())["starboard"].elements()
    client.close()

    for (dbMessage in iterator) {
        if (dbMessage.longValue() == message) {
            return true
        }
    }
    return false
}

fun addToStarboard(message: Long) {
    val client = MongoClients.create()
    val collection = client.getDatabase(DB_NAME).getCollection("Misc")
    val document = collection.find().first()!!

    collection.findOneAndUpdate(document, Updates.addToSet("starboard", message))
    client.close()
}