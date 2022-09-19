package me.nepnep.nepbot.message.command.commands.admin

import me.nepnep.nepbot.database.BlacklistType
import me.nepnep.nepbot.database.addToBlacklist
import me.nepnep.nepbot.database.removeFromBlacklist
import me.nepnep.nepbot.message.command.AbstractCommand
import me.nepnep.nepbot.message.command.Category
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class TryItAndSeeBlacklist : AbstractCommand(
    "tiasblacklist",
    Category.ADMIN,
    "Adds/removes a channel from the \"try it and see\" blacklist",
    Permission.MANAGE_CHANNEL
) {
    override suspend fun execute(args: List<String>, event: MessageReceivedEvent, channel: GuildMessageChannel) {
        when {
            args.isEmpty() -> {
                channel.addToBlacklist(BlacklistType.TRY_IT_AND_SEE)
            }
            args[0] == "remove" -> {
                channel.removeFromBlacklist(BlacklistType.TRY_IT_AND_SEE)
            }
            else -> {
                channel.sendMessage("Invalid usage").queue()
            }
        }
    }
}