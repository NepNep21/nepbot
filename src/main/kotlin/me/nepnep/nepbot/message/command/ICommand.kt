package me.nepnep.nepbot.message.command

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

interface ICommand {
    fun execute(args: List<String>, event: GuildMessageReceivedEvent)

    fun getInvoke(): String

    fun getRequiredPermission() = Permission.MESSAGE_WRITE

    fun getCategory(): Category

    fun getDescription(): String
}