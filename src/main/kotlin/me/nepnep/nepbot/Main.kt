package me.nepnep.nepbot

import com.fasterxml.jackson.databind.ObjectMapper
import com.mongodb.ConnectionString
import com.mongodb.client.MongoClients
import me.nepnep.nepbot.message.Messages
import me.nepnep.nepbot.message.command.CommandRegister
import me.nepnep.nepbot.message.command.CommandResponder
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.MemberCachePolicy
import java.io.File

internal val mongoClient = MongoClients.create(ConnectionString(System.getenv("MONGODB_URL")))
internal val config = ObjectMapper().readTree(File("config.json"))
internal const val DB_NAME = "Nepbot"

fun main() {
    val token = System.getenv("BOT_TOKEN")

    CommandRegister.registerCommands()

    val builder = JDABuilder.createDefault(token)
        .enableIntents(GatewayIntent.GUILD_MEMBERS)
        .setMemberCachePolicy(MemberCachePolicy.ALL)
        .addEventListeners(
            CommandResponder(),
            Messages(),
            ReadyListener(),
            DefaultRole(),
            JoinMessage(),
            LeaveMessage(),
            Starboard()
        )
        .setMaxReconnectDelay(config["reconnectDelay"].intValue())

    val activity = config["activity"]
    val content = activity["content"].textValue()
    when (activity["type"].textValue()) {
        "watching" -> {
            builder.setActivity(Activity.watching(content))
        }
        "competing" -> {
            builder.setActivity(Activity.competing(content))
        }
        "listening" -> {
            builder.setActivity(Activity.listening(content))
        }
        "playing" -> {
            builder.setActivity(Activity.playing(content))
        }
    }
    builder.build()
}

private class ReadyListener : ListenerAdapter() {
    override fun onReady(event: ReadyEvent) {
        Runtime.getRuntime().addShutdownHook(Thread {
            event.jda.shutdown()
            mongoClient.close()
        })
    }
}