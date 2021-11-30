package me.nepnep.nepbot.message.command.commands.info

import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.message.command.AbstractCommand
import net.dv8tion.jda.api.entities.GuildMessageChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class ProfilePicture : AbstractCommand(
    "pfp",
    Category.INFO,
    "Sends the avatar url of a user: ;pfp <Mention member> | <long id> | null"
) {
    override fun execute(args: List<String>, event: MessageReceivedEvent, channel: GuildMessageChannel) {
        val authorUrl = event.author.avatarUrl
        val mentioned = event.message.mentionedMembers

        if (args.isEmpty() && authorUrl != null) {
            channel.sendMessage("$authorUrl?size=2048").queue()
            return
        }
        if (mentioned.isNotEmpty()) {
            val mentionedUrl = mentioned[0].user.avatarUrl
            if (mentionedUrl != null) {
                channel.sendMessage("$mentionedUrl?size=2048").queue()
            }
            return
        }
        try {
            event.jda.retrieveUserById(args[0]).queue({
                val url = it.avatarUrl

                url ?: return@queue

                channel.sendMessage("$url?size=2048").queue()
            }) {
                channel.sendMessage("Invalid id").queue()
            }
        } catch (e: NumberFormatException) {
            channel.sendMessage("Invalid id").queue()
        }
    }
}