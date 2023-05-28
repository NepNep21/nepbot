package me.nepnep.nepbot.message.command.commands

import me.nepnep.nepbot.config
import me.nepnep.nepbot.message.command.AbstractCommand
import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.plugin.PluginManager
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class Unload : AbstractCommand(
    "unload",
    Category.GENERAL,
    "Unload a plugin"
) {
    override suspend fun execute(args: List<String>, event: MessageReceivedEvent, channel: GuildMessageChannel) {
        val operator = config.operator

        if (event.author.idLong != operator) {
            channel.sendMessage("You do not have permission to use this command").queue()
            return
        }

        if (args.isEmpty()) {
            channel.sendMessage("State a name").queue()
            return
        }

        val name = args.first()
        if (PluginManager.contains(name)) {
            PluginManager.unload(name)
        }
    }
}