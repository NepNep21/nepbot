package me.nepnep.nepbot.message.command.commands.mod

import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.message.command.AbstractCommand
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.exceptions.HierarchyException

class Mute : AbstractCommand(
    "mute",
    Category.MOD,
    "Mutes someone: ;mute <Mention member> <String reason>",
    Permission.KICK_MEMBERS
) {
    override fun execute(args: List<String>, event: GuildMessageReceivedEvent) {
        val mentioned = event.message.mentionedMembers
        val channel = event.channel

        if (mentioned.isEmpty() || args.size < 2) {
            channel.sendMessage(":x: Incorrect usage").queue()
            return
        }
        if (mentioned.size > 1) {
            channel.sendMessage(":x: Please only mention one user.").queue()
            return
        }
        val toMute = mentioned[0]
        val guild = event.guild

        if (!event.member!!.canInteract(toMute)) {
            channel.sendMessage(":x: You don't have permission to use this command.").queue()
            return
        }

        val selfMember = guild.selfMember
        if (!selfMember.hasPermission(Permission.MANAGE_ROLES) || !selfMember.canInteract(toMute)) {
            channel.sendMessage(":x: I can't manage roles that user or I don't have permission to manage roles.")
                .queue()
            return
        }

        val roles = guild.getRolesByName("muted", true)

        if (roles.isEmpty()) {
            channel.sendMessage(":x: Unable to find role named \"Muted\" (Not case sensitive)").queue()
            return
        }
        val muted = roles[0]

        try {
            guild.addRoleToMember(toMute, muted).queue()
            channel.sendMessage(
                "${event.author.asTag} muted ${toMute.user.asTag} for reason: ${
                    args.subList(1, args.size).joinToString(" ")
                }"
            ).queue()
        } catch (e: HierarchyException) {
            channel.sendMessage(":x: I can't manage the Muted role, it is higher than my max role!").queue()
        }
    }
}