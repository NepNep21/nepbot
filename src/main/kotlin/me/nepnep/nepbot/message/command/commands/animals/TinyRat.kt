package me.nepnep.nepbot.message.command.commands.animals

import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.message.command.AbstractCommand
import net.dv8tion.jda.api.entities.GuildMessageChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class TinyRat : AbstractCommand(
    "tinyrat",
    Category.ANIMALS,
    "A tiny rat"
) {
    override fun execute(args: List<String>, event: MessageReceivedEvent, channel: GuildMessageChannel) {
        channel.sendMessage("http://mfw.tfw.wtf/").queue()
    }
}