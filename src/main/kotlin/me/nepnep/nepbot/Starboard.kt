package me.nepnep.nepbot

import me.nepnep.nepbot.database.addToStarboard
import me.nepnep.nepbot.database.isInStarboard
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class Starboard : ListenerAdapter() {
    override fun onMessageReactionAdd(event: MessageReactionAddEvent) {
        if (!event.isFromGuild) {
            return
        }
        val guild = event.guild
        val channels = guild.getTextChannelsByName("starboard", false)

        if (channels.isNotEmpty()) {
            val starboard = channels[0]
            val channel = event.channel
            if (!guild.isInStarboard(event.messageIdLong)
                && channel != starboard
                && guild.selfMember.hasPermission(starboard, Permission.MESSAGE_SEND)
            ) {
                event.retrieveMessage().queue { message ->
                    val stars = message.reactions.filter { it.reactionEmote.isEmoji && it.reactionEmote.emoji == "⭐" }

                    if (stars.isNotEmpty()) {
                        val reaction = stars[0]
                        reaction.retrieveUsers().queue {
                            val author = message.author
                            val starCount = if (it.contains(author)) reaction.count - 1 else reaction.count
                            if (starCount == 3) {
                                val builder = EmbedBuilder()
                                    .setAuthor(author.asTag, null, author.avatarUrl)
                                    .setDescription("[Context](${message.jumpUrl})\n${message.contentRaw}")
                                    .setTimestamp(message.timeCreated)
                                val attachments = message.attachments

                                if (attachments.isNotEmpty()) {
                                    builder.setImage(attachments[0].url)
                                }

                                guild.addToStarboard(event.messageIdLong)
                                starboard.sendMessageEmbeds(builder.build()).queue()
                            }
                        }
                    }
                }
            }
        }
    }
}