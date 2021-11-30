package me.nepnep.nepbot.message.command.commands.admin

import me.nepnep.nepbot.database.getDefaultRole
import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.message.command.AbstractCommand
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.GuildMessageChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class GiveDefaultRole : AbstractCommand(
    "givedefaultrole",
    Category.ADMIN,
    "Gives the default role to all members without one",
    Permission.MANAGE_ROLES
) {
    override fun execute(args: List<String>, event: MessageReceivedEvent, channel: GuildMessageChannel) {
        val guild = event.guild
        val role = guild.getDefaultRole() ?: return

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
}