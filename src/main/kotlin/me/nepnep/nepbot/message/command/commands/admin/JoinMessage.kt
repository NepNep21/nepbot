package me.nepnep.nepbot.message.command.commands.admin

import me.nepnep.nepbot.database.setJoinDetails
import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.message.command.AbstractCommand
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

class JoinMessage : AbstractCommand(
    "joinmessage",
    Category.ADMIN,
    ";joinmessage (set <long channelId> <String message (Use %s where you want the user's name to be)>) | remove",
    Permission.MANAGE_CHANNEL
) {
    override fun execute(args: List<String>, event: GuildMessageReceivedEvent) {
        val guild = event.guild
        val channel = event.channel
        try {
            if (args.size >= 3 && args[0] == "set") {
                guild.setJoinDetails(args[1].toLong(), args.subList(2, args.size).joinToString(" "))
            }
        } catch (e: NumberFormatException) {
            channel.sendMessage("Invalid channel id").queue()
        }

        if (args.size == 1 && args[0] == "remove") {
            guild.setJoinDetails(0, "0")
        }
    }
}