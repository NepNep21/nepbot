package me.nepnep.nepbot

import com.fasterxml.jackson.databind.ObjectMapper
import com.mongodb.ConnectionString
import com.mongodb.client.MongoClients
import dev.minn.jda.ktx.events.CoroutineEventListener
import dev.minn.jda.ktx.events.listener
import dev.minn.jda.ktx.jdabuilder.default
import dev.minn.jda.ktx.jdabuilder.intents
import me.nepnep.nepbot.message.command.CommandRegister
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.MemberCachePolicy
import java.io.File
import java.lang.IllegalStateException

internal val mongoClient = MongoClients.create(ConnectionString(System.getenv("MONGODB_URL")))
internal val mongoGuilds = mongoClient.getDatabase("Nepbot").getCollection("Guilds")
internal val config = ObjectMapper().readValue(File("config.json"), Config::class.java)
internal val DEFAULT_PREFIX = config.prefix

fun main() {
    Runtime.getRuntime().addShutdownHook(Thread {
        mongoClient.close()
    })
    val token = System.getenv("BOT_TOKEN")

    CommandRegister.registerCommands()
    
    val jda = default(token) {
        intents += listOf(GatewayIntent.GUILD_MEMBERS, GatewayIntent.MESSAGE_CONTENT)
        setMemberCachePolicy(MemberCachePolicy.ALL)
        setMaxReconnectDelay(config.reconnectDelay)

        val activity = config.activity
        val content = activity.content
        setActivity(when (activity.type) {
            "watching" -> Activity.watching(content)
            "competing" -> Activity.competing(content)
            "listening" -> Activity.listening(content)
            "playing" -> Activity.playing(content)
            else -> null
        })
    }
    jda.listener(consumer = CoroutineEventListener::commandResponder)
    jda.listener(consumer = CoroutineEventListener::defaultRole)
    jda.listener(consumer = CoroutineEventListener::joinMessage)
    jda.listener(consumer = CoroutineEventListener::leaveMessage)
    jda.listener(consumer = CoroutineEventListener::onThreadRevealed)
    jda.listener(consumer = CoroutineEventListener::onThreadHidden)
    jda.listener(consumer = CoroutineEventListener::onMessageReactionAdd)
    jda.listener(consumer = CoroutineEventListener::messages)
}

class Config {
    lateinit var activity: Activity
    var operator = -1L; get() {
        if (field == -1L) {
            throw IllegalStateException("Uninitialized operator")
        }
        
        return field
    }
    val reconnectDelay = 32
    val uncalledMessages = false
    val prefix = ";"
    val uwurandom = false
    
    class Activity {
        lateinit var content: String
        lateinit var type: String
    }
}