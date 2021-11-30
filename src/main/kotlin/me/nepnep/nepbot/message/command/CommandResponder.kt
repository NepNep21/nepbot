package me.nepnep.nepbot.message.command

import me.nepnep.nepbot.canSend
import me.nepnep.nepbot.database.getPrefix
import net.dv8tion.jda.api.entities.GuildMessageChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class CommandResponder : ListenerAdapter() {
    override fun onMessageReceived(event: MessageReceivedEvent) {
        // Ignore private events for now
        if (!event.isFromGuild) {
            return
        }
        val content = event.message.contentRaw
        val guild = event.guild
        val prefix = guild.getPrefix()
        val author = event.author
        val channel = event.channel as GuildMessageChannel

        if (!author.isBot
            && !event.isWebhookMessage
            && content.startsWith(prefix)
            && guild.selfMember.canSend(channel)
        ) {
            val split = content.replaceFirst(prefix, "").split(" ")
            val invoke = split[0].lowercase()

            if (CommandRegister.register.containsKey(invoke)) {
                val command = CommandRegister.register[invoke]!!

                if (event.member!!.hasPermission(channel, command.requiredPermission)) {
                    val args = split.subList(1, split.size)
                    command.execute(args, event, channel)
                } else {
                    channel.sendMessage("Invalid permissions (required ${command.requiredPermission.name})")
                        .queue()
                }
            }
        }
    }
}