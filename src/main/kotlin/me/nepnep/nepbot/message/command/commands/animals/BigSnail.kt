package me.nepnep.nepbot.message.command.commands.animals

import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.message.command.ICommand
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

class BigSnail : ICommand {
    override fun execute(args: List<String>, event: GuildMessageReceivedEvent) {
        event.channel.sendMessage("https://bigsnail.monster/").queue()
    }

    override fun getInvoke() = "bigsnail"

    override fun getCategory() = Category.ANIMALS

    override fun getDescription() = "A big snail"
}