package me.nepnep.nepbot.message.command.commands

import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.message.command.AbstractCommand
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class Ping : AbstractCommand(
    "ping",
    Category.GENERAL,
    "Pong!"
) {
    override suspend fun execute(args: List<String>, event: MessageReceivedEvent, channel: GuildMessageChannel) {
        channel.sendMessage("${event.jda.gatewayPing}ms").queue()
    }
}