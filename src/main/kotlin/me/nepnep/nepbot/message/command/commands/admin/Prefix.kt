package me.nepnep.nepbot.message.command.commands.admin

import me.nepnep.nepbot.database.setPrefix
import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.message.command.AbstractCommand
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class Prefix : AbstractCommand(
    "prefix",
    Category.ADMIN,
    "Sets/unsets the prefix: ;prefix (set <String prefix>) | unset",
    Permission.MANAGE_CHANNEL
) {
    override suspend fun execute(args: List<String>, event: MessageReceivedEvent, channel: GuildMessageChannel) {
        if (args.isEmpty()) {
            channel.sendMessage("Args cannot be empty!").queue()
            return
        }
        val guild = event.guild
        when (args[0]) {
            "set" -> {
                if (args.size < 2) {
                    channel.sendMessage("You must specify a prefix when using 'set'").queue()
                    return
                }
                guild.setPrefix(args[1])
            }
            "unset" -> {
                guild.setPrefix(null)
            }
        }
    }
}