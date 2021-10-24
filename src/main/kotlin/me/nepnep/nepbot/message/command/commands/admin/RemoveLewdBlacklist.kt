package me.nepnep.nepbot.message.command.commands.admin

import me.nepnep.nepbot.database.removeFromBlacklist
import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.message.command.AbstractCommand
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

class RemoveLewdBlacklist : AbstractCommand(
    "removelewdblacklist",
    Category.ADMIN,
    "Removes the current channel from the lewd blacklist",
    Permission.MANAGE_CHANNEL
) {
    override fun execute(args: List<String>, event: GuildMessageReceivedEvent) {
        event.channel.removeFromBlacklist()
    }
}