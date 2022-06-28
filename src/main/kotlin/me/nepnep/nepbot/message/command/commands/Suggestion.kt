package me.nepnep.nepbot.message.command.commands

import dev.minn.jda.ktx.messages.Embed
import me.nepnep.nepbot.message.command.AbstractCommand
import me.nepnep.nepbot.message.command.Category
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.GuildMessageChannel
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class Suggestion : AbstractCommand(
    "suggestion",
    Category.GENERAL,
    "Makes a suggestion: ;suggestion <String suggestion>"
) {
    override suspend fun execute(args: List<String>, event: MessageReceivedEvent, channel: GuildMessageChannel) {
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

        val embed = Embed { 
            title = author.asTag
            thumbnail = author.effectiveAvatarUrl
            description = suggestion
            footer("ID: " + author.id)
        }
        channel.sendMessageEmbeds(embed).queue {
            it.addReaction(Emoji.fromUnicode("✅")).queue()
            it.addReaction(Emoji.fromUnicode("❌")).queue()
        }
        event.message.delete().queue()
    }
}