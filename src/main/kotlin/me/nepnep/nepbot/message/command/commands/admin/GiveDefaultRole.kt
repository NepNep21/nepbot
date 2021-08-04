package me.nepnep.nepbot.message.command.commands.admin

import me.nepnep.nepbot.database.getDefaultRole
import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.message.command.ICommand
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

class GiveDefaultRole : ICommand {
    override fun execute(args: List<String>, event: GuildMessageReceivedEvent) {
        val guild = event.guild
        val role = guild.getDefaultRole() ?: return

        val channel = event.channel
        val selfMember = guild.selfMember
        if (!selfMember.canInteract(role) || !selfMember.hasPermission(Permission.MANAGE_ROLES)) {
            channel.sendMessage("I cannot interact with the default role").queue()
            return
        }
        guild.loadMembers().onSuccess {
            for (member in it) {
                if (member.roles.isEmpty() && selfMember.canInteract(member)) {
                    guild.addRoleToMember(member, role).queue()
                }
            }
        }
    }

    override fun getInvoke(): String {
        return "givedefaultrole"
    }

    override fun getRequiredPermission(): Permission {
        return Permission.MANAGE_ROLES
    }

    override fun getCategory(): Category {
        return Category.ADMIN
    }

    override fun getDescription(): String {
        return "Gives the default role to all members without one"
    }
}