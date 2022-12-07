package me.nepnep.nepbot.plugins.uncalled

import me.nepnep.nepbot.message.command.AbstractCommand
import me.nepnep.nepbot.message.command.Category
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class LewdBlacklist : AbstractCommand(
    "lewdblacklist",
    Category.ADMIN,
    "Adds the current channel to the lewd blacklist",
    Permission.MANAGE_CHANNEL
) {
    override suspend fun execute(args: List<String>, event: MessageReceivedEvent, channel: GuildMessageChannel) {
        channel.addToBlacklist(BlacklistType.LEWD)
    }
}