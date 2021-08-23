package me.nepnep.nepbot.message.command.commands.`fun`

import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.message.command.ICommand
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

class Lmddgtfy : ICommand {
    override fun execute(args: List<String>, event: GuildMessageReceivedEvent) {
        val query = args.subList(0, args.size).joinToString(" ")
        val safe = query.replace(" ", "%20")
        val embed = EmbedBuilder().setDescription("[Result](https://lmddgtfy.net/?q=$safe)").build()

        event.channel.sendMessageEmbeds(embed).queue()
    }

    override fun getInvoke() = "lmddgtfy"

    override fun getCategory() = Category.FUN

    override fun getDescription() = "Searches something for you on duck duck go: ;lmddgtfy <String query>"
}