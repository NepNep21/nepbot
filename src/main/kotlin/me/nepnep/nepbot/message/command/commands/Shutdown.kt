package me.nepnep.nepbot.message.command.commands

import me.nepnep.nepbot.config
import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.message.command.AbstractCommand
import net.dv8tion.jda.api.entities.GuildMessageChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import kotlin.system.exitProcess

class Shutdown : AbstractCommand(
    "shutdown",
    Category.GENERAL,
    "Shuts down the bot"
) {
    override suspend fun execute(args: List<String>, event: MessageReceivedEvent, channel: GuildMessageChannel) {
        val operator = config["operator"].longValue()

        if (event.author.idLong != operator) {
            channel.sendMessage("You do not have permission to use this command").queue()
            return
        }
        channel.sendMessage("Shutting down").complete()
        exitProcess(0)
    }
}