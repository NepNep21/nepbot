package me.nepnep.nepbot.message.command.commands.admin

import me.nepnep.nepbot.database.addToWhitelist
import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.message.command.AbstractCommand
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.GuildMessageChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class BottomWhitelist : AbstractCommand(
    "bottomwhitelist",
    Category.ADMIN,
    "Adds the channel to the bottom whitelist",
    Permission.MANAGE_CHANNEL
) {
    override suspend fun execute(args: List<String>, event: MessageReceivedEvent, channel: GuildMessageChannel) {
        channel.addToWhitelist()
    }
}