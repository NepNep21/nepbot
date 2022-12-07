package me.nepnep.nepbot.message.command.commands

import me.nepnep.nepbot.message.command.AbstractCommand
import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.plugin.PluginManager
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class Info : AbstractCommand(
    "info",
    Category.GENERAL,
    "Gets info about a plugin"
) {
    override suspend fun execute(args: List<String>, event: MessageReceivedEvent, channel: GuildMessageChannel) {
        if (args.isEmpty()) {
            channel.sendMessage("State a name").queue()
            return
        }

        val info = PluginManager.getInfo(args.first())

        if (info == null) {
            channel.sendMessage("Not found").queue()
            return
        }

        channel.sendMessage("Name: ${info.name}\nDescription: ${info.description}\nVersion: ${info.version}").queue()
    }
}