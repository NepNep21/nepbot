@file:Suppress("unused") // Unused receiver
package me.nepnep.nepbot

import dev.minn.jda.ktx.coroutines.await
import dev.minn.jda.ktx.events.CoroutineEventListener
import dev.minn.jda.ktx.messages.Embed
import me.nepnep.nepbot.database.*
import me.nepnep.nepbot.message.command.CommandRegister
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import net.dv8tion.jda.api.events.thread.ThreadHiddenEvent
import net.dv8tion.jda.api.events.thread.ThreadRevealedEvent

suspend fun CoroutineEventListener.commandResponder(event: MessageReceivedEvent) {
    // Ignore private events for now
    if (!event.isFromGuild) {
        return
    }
    val content = event.message.contentRaw
    val guild = event.guild
    val prefix = guild.getPrefix()
    val author = event.author
    val channel = event.channel as GuildMessageChannel

    if (!author.isBot
        && !event.isWebhookMessage
        && content.startsWith(prefix)
        && guild.selfMember.canSend(channel)
    ) {
        val split = content.replaceFirst(prefix, "").split(" ")
        val invoke = split[0].lowercase()

        if (CommandRegister.register.containsKey(invoke)) {
            val command = CommandRegister.register.getValue(invoke)

            if (event.member!!.hasPermission(channel, command.requiredPermission)) {
                val args = split.subList(1, split.size)
                command.execute(args, event, channel)
            } else {
                channel.sendMessage("Invalid permissions (required ${command.requiredPermission.name})").queue()
            }
        }
    }
}

suspend fun CoroutineEventListener.defaultRole(event: GuildMemberJoinEvent) {
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

suspend fun CoroutineEventListener.joinMessage(event: GuildMemberJoinEvent) {
    val guild = event.guild
    val joinDetails = guild.getJoinDetails() ?: return
    val channel = guild.getTextChannelById(joinDetails["channel"].longValue()) ?: return
    val message = joinDetails["message"].textValue()

    if (guild.selfMember.hasPermission(channel, Permission.MESSAGE_SEND)) {
        channel.sendMessage(message.replace("%s", event.user.asMention)).queue()
    }
}

fun CoroutineEventListener.leaveMessage(event: GuildMemberRemoveEvent) {
    val guild = event.guild
    val channels = guild.getTextChannelsByName("goodbye", false)

    if (channels.isNotEmpty()) {
        val channel = channels[0]
        if (guild.selfMember.hasPermission(channel, Permission.MESSAGE_SEND)) {
            channel.sendMessage("${event.user.asTag} just left the server, goodbye.").queue()
        }
    }
}

fun CoroutineEventListener.onThreadRevealed(event: ThreadRevealedEvent) {
    event.thread.join().queue()
}

suspend fun CoroutineEventListener.onThreadHidden(event: ThreadHiddenEvent) {
    val thread = event.thread
    for (type in BlacklistType.values()) {
        thread.removeFromBlacklist(type)
    }
    thread.removeFromWhitelist()
}

suspend fun CoroutineEventListener.onMessageReactionAdd(event: MessageReactionAddEvent) {
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
            val message = event.retrieveMessage().await()
            val stars = message.reactions.filter { it.emoji.type == Emoji.Type.UNICODE && it.emoji.asReactionCode == "⭐" }

            if (stars.isNotEmpty()) {
                val reaction = stars[0]
                val users = reaction.retrieveUsers().await()
                val author = message.author
                val starCount = if (users.contains(author)) reaction.count - 1 else reaction.count
                if (starCount == 3) {
                    val embed = Embed { 
                        author { 
                            name = author.asTag
                            url = author.effectiveAvatarUrl
                        }
                        description = "[Context](${message.jumpUrl})\n${message.contentRaw}"
                        timestamp = message.timeCreated
                        image = message.attachments.firstOrNull()?.url
                    }

                    guild.addToStarboard(event.messageIdLong)
                    starboard.sendMessageEmbeds(embed).queue()
                }
            }
        }
    }
}

suspend fun CoroutineEventListener.messages(event: MessageReceivedEvent) {
    if (!event.isFromGuild) {
        return
    }
    val message = event.message
    val guild = event.guild
    val selfMember = guild.selfMember
    val channel = event.channel as GuildMessageChannel
    val canSend = selfMember.canSend(channel)

    if (!(event.isWebhookMessage || event.author.isBot)) {
        if (message.mentions.members.contains(selfMember) && canSend) {
            channel.sendMessage("My prefix for this guild is ${guild.getPrefix()}").queue()
        }

        if (config.uncalledMessages) {
            val shouldSendLewd = !channel.isInBlacklist(BlacklistType.LEWD)
            val content = message.contentRaw.lowercase()

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