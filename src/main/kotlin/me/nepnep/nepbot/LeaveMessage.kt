package me.nepnep.nepbot

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class LeaveMessage : ListenerAdapter() {
    override fun onGuildMemberRemove(event: GuildMemberRemoveEvent) {
        val guild = event.guild
        val channels = guild.getTextChannelsByName("goodbye", false)

        if (channels.isNotEmpty()) {
            val channel = channels[0]
            if (guild.selfMember.hasPermission(channel, Permission.MESSAGE_SEND)) {
                channel.sendMessage("${event.user.asTag} just left the server, goodbye.").queue()
            }
        }
    }
}