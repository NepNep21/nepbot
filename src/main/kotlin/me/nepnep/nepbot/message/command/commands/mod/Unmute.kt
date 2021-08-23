package me.nepnep.nepbot.message.command.commands.mod

import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.message.command.ICommand
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.exceptions.HierarchyException

class Unmute : ICommand {
    override fun execute(args: List<String>, event: GuildMessageReceivedEvent) {
        val mentioned = event.message.mentionedMembers
        val channel = event.channel

        if (mentioned.isEmpty()) {
            channel.sendMessage(":x: Incorrect usage").queue()
            return
        }
        if (mentioned.size > 1) {
            channel.sendMessage(":x: Please only mention one user.").queue()
            return
        }
        val toUnmute = mentioned[0]
        val guild = event.guild

        if (!event.member!!.canInteract(toUnmute)) {
            channel.sendMessage(":x: You don't have permission to use this command.").queue()
            return
        }

        val selfMember = guild.selfMember
        if (!selfMember.hasPermission(Permission.MANAGE_ROLES) || !selfMember.canInteract(toUnmute)) {
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
            guild.removeRoleFromMember(toUnmute, muted).queue()
            channel.sendMessage("Unmuted ${toUnmute.user.asTag}").queue()
        } catch (e: HierarchyException) {
            channel.sendMessage(":x: I can't manage the Muted role, it is higher than my max role!").queue()
        }

    }

    override fun getInvoke() = "unmute"

    override fun getRequiredPermission() = Permission.KICK_MEMBERS

    override fun getCategory() = Category.MOD

    override fun getDescription() = "Unmutes someone: ;unmute <Mention member>"
}