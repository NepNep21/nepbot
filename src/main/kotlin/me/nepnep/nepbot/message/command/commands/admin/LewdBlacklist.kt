package me.nepnep.nepbot.message.command.commands.admin

import me.nepnep.nepbot.database.addToBlacklist
import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.message.command.ICommand
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

class LewdBlacklist : ICommand {
    override fun execute(args: List<String>, event: GuildMessageReceivedEvent) {
        event.channel.addToBlacklist()
    }

    override fun getInvoke() = "lewdblacklist"

    override fun getRequiredPermission() = Permission.MANAGE_CHANNEL

    override fun getCategory() = Category.ADMIN

    override fun getDescription() = "Adds the current channel to the lewd blacklist"
}