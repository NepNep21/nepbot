package me.nepnep.nepbot.message.command.commands.admin

import me.nepnep.nepbot.database.removeFromWhitelist
import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.message.command.ICommand
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

class RemoveBottomWhitelist : ICommand {
    override fun execute(args: List<String>, event: GuildMessageReceivedEvent) {
        event.channel.removeFromWhitelist()
    }

    override fun getInvoke() = "removebottomwhitelist"

    override fun getRequiredPermission() = Permission.MANAGE_CHANNEL

    override fun getCategory() = Category.ADMIN

    override fun getDescription() = "Removes the channel from the bottom whitelist"
}