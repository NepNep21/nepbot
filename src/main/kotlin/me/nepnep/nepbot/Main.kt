package me.nepnep.nepbot

import com.fasterxml.jackson.databind.ObjectMapper
import com.mongodb.ConnectionString
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import dev.minn.jda.ktx.events.CoroutineEventListener
import dev.minn.jda.ktx.events.listener
import dev.minn.jda.ktx.jdabuilder.default
import dev.minn.jda.ktx.jdabuilder.intents
import kotlinx.coroutines.*
import me.nepnep.nepbot.message.command.CommandRegister
import me.nepnep.nepbot.music.MusicManager
import me.nepnep.nepbot.plugin.PluginManager
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.events.session.ReadyEvent
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.MemberCachePolicy
import org.bson.Document
import java.io.File
import kotlin.time.DurationUnit
import kotlin.time.toDuration

val mongoClient: MongoClient = MongoClients.create(ConnectionString(System.getenv("MONGODB_URL")))
val mongoGuilds: MongoCollection<Document> = mongoClient.getDatabase("Nepbot").getCollection("Guilds")
val mapper = ObjectMapper()
val config: Config = mapper.readValue(File("config.json"), Config::class.java)
val DEFAULT_PREFIX = config.prefix
lateinit var jda: JDA

fun main() {
    Runtime.getRuntime().addShutdownHook(Thread {
        mongoClient.close()
    })
    val token = System.getenv("BOT_TOKEN")

    CommandRegister.registerCommands()
    
    jda = default(token) {
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
        
        addEventListeners(ReadyListener)
    }
    
    Runtime.getRuntime().addShutdownHook(Thread {
        ReadyListener.cancel()
    })
    
    jda.listener(consumer = CoroutineEventListener::commandResponder)
    jda.listener(consumer = CoroutineEventListener::defaultRole)
    jda.listener(consumer = CoroutineEventListener::joinMessage)
    jda.listener(consumer = CoroutineEventListener::leaveMessage)
    jda.listener(consumer = CoroutineEventListener::onThreadRevealed)
    jda.listener(consumer = CoroutineEventListener::onMessageReactionAdd)
    jda.listener(consumer = CoroutineEventListener::messages)
    jda.listener(consumer = CoroutineEventListener::onThreadHidden)
    
    PluginManager.loadAll()
}

private object ReadyListener : CoroutineEventListener {
    private var job: Job? = null
    override suspend fun onEvent(event: GenericEvent) {
        if (event is ReadyEvent) {
            job = coroutineScope {
                launch {
                    while (isActive) {
                        MusicManager.players.filter {
                            val player = it.value
                            return@filter (player.getVoiceChannel()?.members?.isEmpty() ?: true)
                        }.forEach { MusicManager.players.remove(it.key) }
                        delay(1.toDuration(DurationUnit.MINUTES))
                    }
                }
            }
        }
    }

    override fun cancel() {
        job?.cancel()
    }
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
    val prefix = ";"
    
    class Activity {
        lateinit var content: String
        lateinit var type: String
    }
}