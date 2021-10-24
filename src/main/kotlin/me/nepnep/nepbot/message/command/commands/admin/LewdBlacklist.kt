package me.nepnep.nepbot.message.command.commands.admin

import me.nepnep.nepbot.database.addToBlacklist
import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.message.command.AbstractCommand
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

class LewdBlacklist : AbstractCommand(
    "lewdblacklist",
    Category.ADMIN,
    "Adds the current channel to the lewd blacklist",
    Permission.MANAGE_CHANNEL
) {
    override fun execute(args: List<String>, event: GuildMessageReceivedEvent) {
        event.channel.addToBlacklist()
    }
}