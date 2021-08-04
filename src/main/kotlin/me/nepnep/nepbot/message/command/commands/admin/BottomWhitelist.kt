package me.nepnep.nepbot.message.command.commands.admin

import me.nepnep.nepbot.database.addToWhitelist
import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.message.command.ICommand
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

class BottomWhitelist : ICommand {
    override fun execute(args: List<String>, event: GuildMessageReceivedEvent) {
        event.channel.addToWhitelist()
    }

    override fun getInvoke(): String {
        return "bottomwhitelist"
    }

    override fun getRequiredPermission(): Permission {
        return Permission.MANAGE_CHANNEL
    }

    override fun getCategory(): Category {
        return Category.ADMIN
    }

    override fun getDescription(): String {
        return "Adds the channel to the bottom whitelist"
    }
}