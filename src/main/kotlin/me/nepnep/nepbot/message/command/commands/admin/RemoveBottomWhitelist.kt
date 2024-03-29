package me.nepnep.nepbot.message.command.commands.admin

import me.nepnep.nepbot.database.removeFromWhitelist
import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.message.command.AbstractCommand
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class RemoveBottomWhitelist : AbstractCommand(
    "removebottomwhitelist",
    Category.ADMIN,
    "Removes the channel from the bottom whitelist",
    Permission.MANAGE_CHANNEL
) {
    override suspend fun execute(args: List<String>, event: MessageReceivedEvent, channel: GuildMessageChannel) {
        channel.removeFromWhitelist()
    }
}