package me.nepnep.nepbot

import me.nepnep.nepbot.database.getDefaultRole
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class DefaultRole : ListenerAdapter() {
    override fun onGuildMemberJoin(event: GuildMemberJoinEvent) {
        val guild = event.guild
        val role = guild.getDefaultRole() ?: return

        val member = event.member
        val selfMember = guild.selfMember
        if (selfMember.hasPermission(Permission.MANAGE_ROLES)
            && selfMember.canInteract(member)
            && selfMember.canInteract(role)
        ) {
            guild.addRoleToMember(member, role).queue()
        }
    }
}