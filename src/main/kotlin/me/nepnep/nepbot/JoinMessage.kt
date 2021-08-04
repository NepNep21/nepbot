package me.nepnep.nepbot

import me.nepnep.nepbot.database.getJoinDetails
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class JoinMessage : ListenerAdapter() {
    override fun onGuildMemberJoin(event: GuildMemberJoinEvent) {
        val guild = event.guild
        val joinDetails = guild.getJoinDetails() ?: return
        val channel = guild.getTextChannelById(joinDetails["channel"].longValue()) ?: return
        val message = joinDetails["message"].textValue()

        channel.sendMessage(message.replace("%s", event.user.asMention)).queue()
    }
}