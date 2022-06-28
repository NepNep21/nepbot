package me.nepnep.nepbot.message.command.commands

import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.message.command.AbstractCommand
import net.dv8tion.jda.api.entities.GuildMessageChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import java.util.concurrent.ThreadLocalRandom

class Poll : AbstractCommand(
    "poll",
    Category.GENERAL,
    "Gets a random answer from options: ;poll <String... options>"
) {
    override suspend fun execute(args: List<String>, event: MessageReceivedEvent, channel: GuildMessageChannel) {
        if (event.message.mentions.mentionsEveryone()) {
            channel.sendMessage("Nice atEveryone attempt").queue()
            return
        }

        if (args.isEmpty()) {
            channel.sendMessage("Arguments must not be empty").queue()
            return
        }
        channel.sendMessage(args[ThreadLocalRandom.current().nextInt(0, args.size)]).queue()
    }
}