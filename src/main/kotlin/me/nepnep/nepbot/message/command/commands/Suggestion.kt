package me.nepnep.nepbot.message.command.commands

import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.message.command.AbstractCommand
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.GuildMessageChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class Suggestion : AbstractCommand(
    "suggestion",
    Category.GENERAL,
    "Makes a suggestion: ;suggestion <String suggestion>"
) {
    override fun execute(args: List<String>, event: MessageReceivedEvent, channel: GuildMessageChannel) {
        if (channel.name != "suggestions") {
            channel.sendMessage("Channel not named `suggestions`!").queue()
            return
        }
        if (args.isEmpty()) {
            channel.sendMessage("Invalid usage!").queue()
            return
        }
        if (!event.guild.selfMember.hasPermission(channel, Permission.MESSAGE_MANAGE)) {
            channel.sendMessage("I do not have permission to delete messages in this channel").queue()
            return
        }

        val suggestion = args.subList(0, args.size).joinToString(" ")
        val author = event.author

        val embed = EmbedBuilder()
            .setTitle(author.asTag)
            .setThumbnail(author.effectiveAvatarUrl)
            .setDescription(suggestion)
            .setFooter("ID: " + author.id)
            .build()
        channel.sendMessageEmbeds(embed).queue {
            it.addReaction("✅").queue()
            it.addReaction("❌").queue()
        }
        event.message.delete().queue()
    }
}