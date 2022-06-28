package me.nepnep.nepbot.message.command.commands.admin

import me.nepnep.nepbot.database.setDefaultRole
import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.message.command.AbstractCommand
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.GuildMessageChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class SetDefaultRole : AbstractCommand(
    "setdefaultrole",
    Category.ADMIN,
    "Sets the default role: ;setdefaultrole <Mention role> or empty arguments for unsetting",
    Permission.MANAGE_ROLES
) {
    override suspend fun execute(args: List<String>, event: MessageReceivedEvent, channel: GuildMessageChannel) {
        val mentionedRoles = event.message.mentions.roles
        val guild = event.guild
        if (mentionedRoles.isEmpty()) {
            guild.setDefaultRole(null)
        } else {
            guild.setDefaultRole(mentionedRoles[0])
        }
    }
}