package me.nepnep.nepbot.message.command.commands

import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.message.command.ICommand
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

class Ping : ICommand {
    override fun execute(args: List<String>, event: GuildMessageReceivedEvent) {
        event.channel.sendMessage("${event.jda.gatewayPing}ms").queue()
    }

    override fun getInvoke() = "ping"

    override fun getCategory() = Category.GENERAL

    override fun getDescription() = "Pong!"
}