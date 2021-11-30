package me.nepnep.nepbot.message.command.commands.`fun`

import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.message.command.AbstractCommand
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.GuildMessageChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class Lmddgtfy : AbstractCommand(
    "lmddgtfy",
    Category.FUN,
    "Searches something for you on duck duck go: ;lmddgtfy <String query>"
) {
    override fun execute(args: List<String>, event: MessageReceivedEvent, channel: GuildMessageChannel) {
        val query = args.subList(0, args.size).joinToString(" ")
        val safe = query.replace(" ", "%20")
        val embed = EmbedBuilder().setDescription("[Result](https://lmddgtfy.net/?q=$safe)").build()

        channel.sendMessageEmbeds(embed).queue()
    }
}