package me.nepnep.nepbot.message.command.commands.admin

import me.nepnep.nepbot.database.setDefaultRole
import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.message.command.ICommand
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

class SetDefaultRole : ICommand {
    override fun execute(args: List<String>, event: GuildMessageReceivedEvent) {
        val mentionedRoles = event.message.mentionedRoles
        val guild = event.guild
        if (mentionedRoles.isEmpty()) {
            guild.setDefaultRole(null)
        } else {
            guild.setDefaultRole(mentionedRoles[0])
        }
    }

    override fun getInvoke(): String {
        return "setdefaultrole"
    }

    override fun getRequiredPermission(): Permission {
        return Permission.MANAGE_ROLES
    }

    override fun getCategory(): Category {
        return Category.ADMIN
    }

    override fun getDescription(): String {
        return "Sets the default role: ;setdefaultrole <Mention role> or empty arguments for unsetting"
    }
}