package me.nepnep.nepbot.message.command

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class CommandResponder : ListenerAdapter() {
    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
        val content = event.message.contentRaw
        val guild = event.guild
        val prefix = ";"
        val author = event.author
        val channel = event.channel

        if (!author.isBot
            && !event.isWebhookMessage
            && content.startsWith(prefix)
            && guild.selfMember.hasPermission(channel, Permission.MESSAGE_WRITE)
        ) {

            val split = content.replaceFirst(prefix, "").split(" ")
            val invoke = split[0].lowercase()

            if (CommandRegister.register.containsKey(invoke)) {
                val command = CommandRegister.register[invoke]!!

                if (event.member!!.hasPermission(channel, command.getRequiredPermission())) {
                    val args = split.subList(1, split.size)
                    command.execute(args, event)
                } else {
                    channel.sendMessage("Invalid permissions (required ${command.getRequiredPermission().name})")
                        .queue()
                }
            }
        }
    }
}