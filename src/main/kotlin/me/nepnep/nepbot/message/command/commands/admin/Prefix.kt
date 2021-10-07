package me.nepnep.nepbot.message.command.commands.admin

import me.nepnep.nepbot.database.setPrefix
import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.message.command.ICommand
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

class Prefix : ICommand {
    override fun execute(args: List<String>, event: GuildMessageReceivedEvent) {
        val channel = event.channel
        if (args.isEmpty()) {
            channel.sendMessage("Args cannot be empty!").queue()
            return
        }
        val guild = event.guild
        when (args[0]) {
            "set" -> {
                if (args.size < 2) {
                    channel.sendMessage("You must specify a prefix when using 'set'")
                    return
                }
                guild.setPrefix(args[1])
            }
            "unset" -> {
                guild.setPrefix(null)
            }
        }
    }

    override fun getInvoke() = "prefix"

    override fun getRequiredPermission() = Permission.MANAGE_CHANNEL

    override fun getCategory() = Category.ADMIN

    override fun getDescription() = "Sets/unsets the prefix: ;prefix (set <String prefix>) | unset"
}