package me.nepnep.nepbot.message.command.commands.animals

import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.message.command.ICommand
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

class XmasRat : ICommand {
    override fun execute(args: List<String>, event: GuildMessageReceivedEvent) {
        event.channel.sendMessage("https://christmas.bigrat.monster/").queue()
    }

    override fun getInvoke(): String {
        return "xmasrat"
    }

    override fun getCategory(): Category {
        return Category.ANIMALS
    }

    override fun getDescription(): String {
        return "A christmas rat"
    }
}