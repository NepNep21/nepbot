package me.nepnep.nepbot.message.command.commands.animals

import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.message.command.ICommand
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

class TinySnail : ICommand {
    override fun execute(args: List<String>, event: GuildMessageReceivedEvent) {
        event.channel.sendMessage("http://tinysnail.monster/").queue()
    }

    override fun getInvoke() = "tinysnail"

    override fun getCategory() = Category.ANIMALS

    override fun getDescription() = "A tiny snail"
}