package me.nepnep.nepbot.message.command.commands

import me.nepnep.nepbot.config
import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.message.command.AbstractCommand
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import kotlin.system.exitProcess

class Shutdown : AbstractCommand(
    "shutdown",
    Category.GENERAL,
    "Shuts down the bot"
) {
    override fun execute(args: List<String>, event: GuildMessageReceivedEvent) {
        val channel = event.channel
        val operator = config["operator"].longValue()

        if (event.author.idLong != operator) {
            channel.sendMessage("You do not have permission to use this command").queue()
            return
        }
        channel.sendMessage("Shutting down").complete()
        exitProcess(0)
    }
}