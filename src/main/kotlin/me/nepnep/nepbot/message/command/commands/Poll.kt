package me.nepnep.nepbot.message.command.commands

import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.message.command.ICommand
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import java.util.concurrent.ThreadLocalRandom

class Poll : ICommand {
    override fun execute(args: List<String>, event: GuildMessageReceivedEvent) {
        event.channel.sendMessage(args[ThreadLocalRandom.current().nextInt(0, args.size)]).queue()
    }

    override fun getInvoke(): String {
        return "poll"
    }

    override fun getCategory(): Category {
        return Category.GENERAL
    }

    override fun getDescription(): String {
        return "Gets a random answer from options: ;poll <String... options>"
    }
}