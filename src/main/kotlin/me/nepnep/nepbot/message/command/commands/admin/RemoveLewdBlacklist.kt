package me.nepnep.nepbot.message.command.commands.admin

import me.nepnep.nepbot.database.BlacklistType
import me.nepnep.nepbot.database.removeFromBlacklist
import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.message.command.AbstractCommand
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.GuildMessageChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class RemoveLewdBlacklist : AbstractCommand(
    "removelewdblacklist",
    Category.ADMIN,
    "Removes the current channel from the lewd blacklist",
    Permission.MANAGE_CHANNEL
) {
    override fun execute(args: List<String>, event: MessageReceivedEvent, channel: GuildMessageChannel) {
        channel.removeFromBlacklist(BlacklistType.LEWD)
    }
}