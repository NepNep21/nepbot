package me.nepnep.nepbot.plugins.uncalled

import me.nepnep.nepbot.message.command.AbstractCommand
import me.nepnep.nepbot.message.command.Category
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class RemoveLewdBlacklist : AbstractCommand(
    "removelewdblacklist",
    Category.ADMIN,
    "Removes the current channel from the lewd blacklist",
    Permission.MANAGE_CHANNEL
) {
    override suspend fun execute(args: List<String>, event: MessageReceivedEvent, channel: GuildMessageChannel) {
        channel.removeFromBlacklist(BlacklistType.LEWD)
    }
}