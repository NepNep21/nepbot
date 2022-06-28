package me.nepnep.nepbot.message.command

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.GuildMessageChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

abstract class AbstractCommand(
    val invoke: String,
    val category: Category,
    val description: String,
    val requiredPermission: Permission = Permission.MESSAGE_SEND
) {
    abstract suspend fun execute(args: List<String>, event: MessageReceivedEvent, channel: GuildMessageChannel)
}