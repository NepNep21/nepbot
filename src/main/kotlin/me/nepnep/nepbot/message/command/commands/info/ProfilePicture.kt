package me.nepnep.nepbot.message.command.commands.info

import dev.minn.jda.ktx.coroutines.await
import me.nepnep.nepbot.message.command.AbstractCommand
import me.nepnep.nepbot.message.command.Category
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class ProfilePicture : AbstractCommand(
    "pfp",
    Category.INFO,
    "Sends the avatar url of a user: ;pfp <Mention member> | <long id> | null"
) {
    override suspend fun execute(args: List<String>, event: MessageReceivedEvent, channel: GuildMessageChannel) {
        val authorUrl = event.author.effectiveAvatarUrl
        val mentioned = event.message.mentions.members

        if (args.isEmpty()) {
            channel.sendMessage("$authorUrl?size=2048").queue()
            return
        }
        if (mentioned.isNotEmpty()) {
            val mentionedUrl = mentioned[0].user.effectiveAvatarUrl
            channel.sendMessage("$mentionedUrl?size=2048").queue()
            return
        }
        try {
            val url = event.jda.retrieveUserById(args[0]).await().effectiveAvatarUrl
            channel.sendMessage("$url?size=2048").queue()
        } catch (e: RuntimeException) { // ErrorResponseException | NumberFormatException
            channel.sendMessage("Invalid id").queue()
        }
    }
}