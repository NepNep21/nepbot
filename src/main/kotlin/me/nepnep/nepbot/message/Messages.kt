package me.nepnep.nepbot.message

import me.nepnep.nepbot.canSend
import me.nepnep.nepbot.config
import me.nepnep.nepbot.database.BlacklistType
import me.nepnep.nepbot.database.getPrefix
import me.nepnep.nepbot.database.isInBlacklist
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.GuildMessageChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class Messages : ListenerAdapter() {
    override fun onMessageReceived(event: MessageReceivedEvent) {
        if (!event.isFromGuild) {
            return
        }
        val message = event.message
        val guild = event.guild
        val selfMember = guild.selfMember
        val channel = event.channel as GuildMessageChannel
        val canSend = selfMember.canSend(channel)

        if (!(event.isWebhookMessage || event.author.isBot)) {
            if (message.mentionedMembers.contains(selfMember) && canSend) {
                channel.sendMessage("My prefix for this guild is ${guild.getPrefix()}").queue()
            }

            if (config["uncalledMessages"].booleanValue()) {
                val shouldSendLewd = !channel.isInBlacklist(BlacklistType.LEWD)
                val content = message.contentRaw.lowercase()

                if (content.contains("nigger") && selfMember.hasPermission(channel, Permission.MESSAGE_MANAGE)) {
                    event.message.delete().queue()
                }

                if (canSend) {
                    if (content.contains("communism") || content.contains("socialism")) {
                        channel.sendMessage("https://www.youtube.com/watch?v=gnXUFXc2Yns&ab_channel=ComradeLuigi")
                            .queue()
                    }
                    if ((content.contains("lewd")
                                || content.contains("sex")
                                || content.contains("cum")
                                || content.contains("semen")
                                || content.contains("penis")) && shouldSendLewd
                    ) {
                        channel.sendMessage("https://tenor.com/view/neptunia-gif-18952040").queue()
                    }
                    if (content.contains("what if") && !channel.isInBlacklist(BlacklistType.TRY_IT_AND_SEE)) {
                        channel.sendMessage("https://tryitands.ee/").queue()
                    }
                }
            }
        }
    }
}