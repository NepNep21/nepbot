package me.nepnep.nepbot.message

import me.nepnep.nepbot.config
import me.nepnep.nepbot.database.isInBlacklist
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class Messages : ListenerAdapter() {
    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
        if (config["uncalledMessages"].booleanValue()
            && !(event.isWebhookMessage || event.author.isBot)
        ) {
            val channel = event.channel
            val shouldSendLewd = !channel.isInBlacklist()
            val content = event.message.contentRaw.lowercase()
            val selfMember = event.guild.selfMember

            if (content.contains("nigger") && selfMember.hasPermission(channel, Permission.MESSAGE_MANAGE)) {
                event.message.delete().queue()
            }

            if (selfMember.hasPermission(channel, Permission.MESSAGE_WRITE)) {
                if (content.contains("communism") || content.contains("socialism")) {
                    channel.sendMessage("https://www.youtube.com/watch?v=gnXUFXc2Yns&ab_channel=ComradeLuigi").queue()
                }
                if ((content.contains("lewd")
                            || content.contains("sex")
                            || content.contains("cum")
                            || content.contains("semen")
                            || content.contains("penis")) && shouldSendLewd
                ) {
                    channel.sendMessage("https://tenor.com/view/neptunia-gif-18952040").queue()
                }
                if (content.contains("what if")) {
                    channel.sendMessage("https://tryitands.ee/").queue()
                }
            }
        }
    }
}