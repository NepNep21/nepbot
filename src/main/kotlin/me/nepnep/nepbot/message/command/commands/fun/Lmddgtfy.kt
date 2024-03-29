package me.nepnep.nepbot.message.command.commands.`fun`

import dev.minn.jda.ktx.messages.Embed
import me.nepnep.nepbot.message.command.AbstractCommand
import me.nepnep.nepbot.message.command.Category
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class Lmddgtfy : AbstractCommand(
    "lmddgtfy",
    Category.FUN,
    "Searches something for you on duck duck go: ;lmddgtfy <String query>"
) {
    override suspend fun execute(args: List<String>, event: MessageReceivedEvent, channel: GuildMessageChannel) {
        val query = args.subList(0, args.size).joinToString(" ")
        val safe = query.replace(" ", "%20")

        channel.sendMessageEmbeds(Embed { description = "[Result](https://lmddgtfy.net/?q=$safe)" }).queue()
    }
}