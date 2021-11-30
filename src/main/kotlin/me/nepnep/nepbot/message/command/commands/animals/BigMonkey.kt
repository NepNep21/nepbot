package me.nepnep.nepbot.message.command.commands.animals

import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.message.command.AbstractCommand
import net.dv8tion.jda.api.entities.GuildMessageChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class BigMonkey : AbstractCommand(
    "bigmonkey",
    Category.ANIMALS,
    "A big monkey"
) {
    override fun execute(args: List<String>, event: MessageReceivedEvent, channel: GuildMessageChannel) {
        channel.sendMessage("https://bigmonkey.monster/").queue()
    }
}