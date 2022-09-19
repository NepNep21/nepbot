package me.nepnep.nepbot.message.command.commands.animals

import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.message.command.AbstractCommand
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class XmasRat : AbstractCommand(
    "xmasrat",
    Category.ANIMALS,
    "A christmas rat"
) {
    override suspend fun execute(args: List<String>, event: MessageReceivedEvent, channel: GuildMessageChannel) {
        channel.sendMessage("https://christmas.bigrat.monster/").queue()
    }
}