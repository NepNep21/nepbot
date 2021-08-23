package me.nepnep.nepbot.message.command.commands.admin

import me.nepnep.nepbot.database.removeFromBlacklist
import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.message.command.ICommand
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

class RemoveLewdBlacklist : ICommand {
    override fun execute(args: List<String>, event: GuildMessageReceivedEvent) {
        event.channel.removeFromBlacklist()
    }

    override fun getInvoke() = "removelewdblacklist"

    override fun getRequiredPermission() = Permission.MANAGE_CHANNEL

    override fun getCategory() = Category.ADMIN

    override fun getDescription() = "Removes the current channel from the lewd blacklist"
}