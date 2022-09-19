package me.nepnep.nepbot.message.command.commands.mod

import me.nepnep.nepbot.message.command.AbstractCommand
import me.nepnep.nepbot.message.command.Category
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class Unmute : AbstractCommand(
    "unmute",
    Category.MOD,
    "Unmutes someone: ;unmute <Mention member>",
    Permission.MODERATE_MEMBERS
) {
    override suspend fun execute(args: List<String>, event: MessageReceivedEvent, channel: GuildMessageChannel) {
        val mentioned = event.message.mentions.members

        if (mentioned.isEmpty()) {
            channel.sendMessage(":x: Incorrect usage.").queue()
            return
        }
        if (mentioned.size > 1) {
            channel.sendMessage(":x: Please only mention one member.").queue()
            return
        }
        val toUnmute = mentioned[0]
        if (!toUnmute.isTimedOut) {
            channel.sendMessage(":x: Member is not timed out.").queue()
            return
        }

        val selfMember = event.guild.selfMember
        if (!selfMember.hasPermission(Permission.MODERATE_MEMBERS) || !selfMember.canInteract(toUnmute)) {
            channel.sendMessage(":x: I can't unmute that user or I don't have permission to unmute.").queue()
            return
        }

        toUnmute.removeTimeout().queue()
        channel.sendMessage("Unmuted ${toUnmute.user.asTag}.").queue()
    }
}