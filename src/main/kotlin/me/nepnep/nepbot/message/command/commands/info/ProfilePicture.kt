package me.nepnep.nepbot.message.command.commands.info

import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.message.command.ICommand
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

class ProfilePicture : ICommand {
    override fun execute(args: List<String>, event: GuildMessageReceivedEvent) {
        val channel = event.channel
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

    override fun getInvoke(): String {
        return "pfp"
    }

    override fun getCategory(): Category {
        return Category.INFO
    }

    override fun getDescription(): String {
        return "Sends the avatar url of a user: ;pfp <Mention member> | <long id> | null"
    }
}