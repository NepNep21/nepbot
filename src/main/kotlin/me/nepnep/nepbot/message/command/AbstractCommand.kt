package me.nepnep.nepbot.message.command

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

abstract class AbstractCommand(
    val invoke: String,
    val category: Category,
    val description: String,
    val requiredPermission: Permission = Permission.MESSAGE_WRITE
) {
    abstract fun execute(args: List<String>, event: GuildMessageReceivedEvent)
}