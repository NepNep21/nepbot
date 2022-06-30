package me.nepnep.nepbot.message.command.commands.info

import dev.minn.jda.ktx.coroutines.await
import dev.minn.jda.ktx.messages.EmbedBuilder
import me.nepnep.nepbot.message.command.AbstractCommand
import me.nepnep.nepbot.message.command.Category
import net.dv8tion.jda.api.entities.GuildMessageChannel
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.exceptions.ErrorResponseException
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class WhoIs : AbstractCommand(
    "whois",
    Category.INFO,
    "Gets a user's information: ;whois <Mention member> | <long id> | null"
) {
    override suspend fun execute(args: List<String>, event: MessageReceivedEvent, channel: GuildMessageChannel) {
        val mentioned = event.message.mentions.members
        if (mentioned.isNotEmpty()) {
            sendEmbed(mentioned[0].user, event)
            return
        }

        if (args.isNotEmpty()) {
            try {
                val user = event.jda.retrieveUserById(args[0]).await()
                sendEmbed(user, event)
            } catch (e: RuntimeException) { //  ErrorResponseException | NumberFormatException
                channel.sendMessage("Invalid id!").queue()
            }
            return
        }
        sendEmbed(event.author, event)
    }

    private suspend fun sendEmbed(user: User, event: MessageReceivedEvent) {
        val channel = event.channel
        val now = LocalDate.now()

        val id = user.id
        val tag = user.asTag
        val avatarUrl = user.effectiveAvatarUrl

        val timeCreated = user.timeCreated
        val sinceCreated = ChronoUnit.DAYS.between(timeCreated.toLocalDate(), now)

        val builder = EmbedBuilder {
            title = "$id/$tag"
            thumbnail = avatarUrl
            field("Created on: ", "${timeCreated.dayOfMonth}-${timeCreated.monthValue}-${timeCreated.year}", false)
            field("Days since created: ", sinceCreated.toString(), false)
        }
        try {
            val member = event.guild.retrieveMember(user).await()
            val timeJoined = member.timeJoined
            val sinceJoined = ChronoUnit.DAYS.between(timeJoined.toLocalDate(), now)

            val roles = member.roles.map { it.name }
            val permissions = member.permissions.map { it.name }

            val embed = builder.apply { 
                field("Joined on: ", "${timeJoined.dayOfMonth}-${timeJoined.monthValue}-${timeJoined.year}", false)
                field("Days since joined: ", sinceJoined.toString(), false)
                field("Roles: ", roles.toString(), false)
                field("Permissions: ", permissions.toString(), false)
            }.build()
            if (embed.isSendable) {
                channel.sendMessageEmbeds(embed).queue()
                return
            }
            channel.sendMessage("Embed is too large!").queue()
        } catch (e: ErrorResponseException) {
            channel.sendMessageEmbeds(builder.apply { field("Not in guild", ":x:", false) }.build()).queue()
        }
    }
}
