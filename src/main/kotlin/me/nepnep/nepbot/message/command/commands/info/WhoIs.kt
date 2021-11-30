package me.nepnep.nepbot.message.command.commands.info

import me.nepnep.nepbot.message.command.AbstractCommand
import me.nepnep.nepbot.message.command.Category
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.GuildMessageChannel
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class WhoIs : AbstractCommand(
    "whois",
    Category.INFO,
    "Gets a user's information: ;whois <Mention member> | <long id> | nul"
) {
    override fun execute(args: List<String>, event: MessageReceivedEvent, channel: GuildMessageChannel) {
        val mentioned = event.message.mentionedMembers
        if (mentioned.isNotEmpty()) {
            sendEmbed(mentioned[0].user, event)
            return
        }

        if (args.isNotEmpty()) {
            try {
                event.jda.retrieveUserById(args[0]).queue({
                    sendEmbed(it, event)
                }) {
                    channel.sendMessage("Invalid id!").queue()
                }
            } catch (e: NumberFormatException) {
                channel.sendMessage("Invalid id!").queue()
            }
            return
        }
        sendEmbed(event.author, event)
    }

    private fun sendEmbed(user: User, event: MessageReceivedEvent) {
        val channel = event.channel
        val now = LocalDate.now()

        val id = user.id
        val tag = user.asTag
        val avatarUrl = user.avatarUrl

        val timeCreated = user.timeCreated
        val sinceCreated = ChronoUnit.DAYS.between(timeCreated.toLocalDate(), now)

        val builder = EmbedBuilder()
            .setTitle("$id/$tag")
            .setThumbnail(avatarUrl)
            .addField("Created on: ", "${timeCreated.dayOfMonth}-${timeCreated.monthValue}-${timeCreated.year}", false)
            .addField("Days since created: ", sinceCreated.toString(), false)
        event.guild.retrieveMember(user).queue({ member ->
            val timeJoined = member.timeJoined
            val sinceJoined = ChronoUnit.DAYS.between(timeJoined.toLocalDate(), now)

            val roles = member.roles.map { it.name }
            val permissions = member.permissions.map { it.name }

            val embed = builder
                .addField("Joined on: ", "${timeJoined.dayOfMonth}-${timeJoined.monthValue}-${timeJoined.year}", false)
                .addField("Days since joined: ", sinceJoined.toString(), false)
                .addField("Roles: ", roles.toString(), false)
                .addField("Permissions: ", permissions.toString(), false)
                .build()
            if (embed.isSendable) {
                channel.sendMessageEmbeds(embed).queue()
                return@queue
            }
            channel.sendMessage("Embed is too large!").queue()
        }) {
            val embed = builder
                .addField("Not in guild", ":x:", false)
                .build()
            channel.sendMessageEmbeds(embed).queue()
        }
    }
}