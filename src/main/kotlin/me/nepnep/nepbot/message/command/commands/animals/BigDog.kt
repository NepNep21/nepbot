package me.nepnep.nepbot.message.command.commands.animals

import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.message.command.ICommand
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

class BigDog : ICommand {
    override fun execute(args: List<String>, event: GuildMessageReceivedEvent) {
        event.channel.sendMessage("https://bigdog.monster/").queue()
    }

    override fun getInvoke(): String {
        return "bigdog"
    }

    override fun getCategory(): Category {
        return Category.ANIMALS
    }

    override fun getDescription(): String {
        return "A big dog"
    }
}