package me.nepnep.nepbot.message.command.commands.animals

import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.message.command.AbstractCommand
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

class BigRat : AbstractCommand(
    "bigrat",
    Category.ANIMALS,
    "A big rat"
) {
    override fun execute(args: List<String>, event: GuildMessageReceivedEvent) {
        event.channel.sendMessage("https://bigrat.monster/").queue()
    }
}