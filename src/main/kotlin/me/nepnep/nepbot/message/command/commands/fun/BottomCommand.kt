package me.nepnep.nepbot.message.command.commands.`fun`

import com.github.bottomSoftwareFoundation.bottom.Bottom
import com.github.bottomSoftwareFoundation.bottom.TranslationError
import me.nepnep.nepbot.database.isInWhitelist
import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.message.command.AbstractCommand
import net.dv8tion.jda.api.entities.GuildMessageChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class BottomCommand : AbstractCommand(
    "bottom",
    Category.FUN,
    "Encodes/Decodes to bottom: ;bottom <encode> | <decode> <String string>"
) {
    override suspend fun execute(args: List<String>, event: MessageReceivedEvent, channel: GuildMessageChannel) {
        if (channel.isInWhitelist() && args.size >= 2) {
            val string = args.subList(1, args.size).joinToString(" ")

            if (string.length >= 2000) {
                channel.sendMessage("Resulting message must be <2000 characters").queue()
                return
            }

            val operation = args[0]
            try {
                if (operation == "encode") {
                    channel.sendMessage(Bottom.encode(string)).queue()
                }
                if (operation == "decode") {
                    val decoded = Bottom.decode(string)
                    if (decoded.contains("@everyone") || decoded.contains("@here")) {
                        channel.sendMessage("Nice atEveryone attempt").queue()
                        return
                    }
                    channel.sendMessage(Bottom.decode(string)).queue()
                }
            } catch (e: TranslationError) {
                channel.sendMessage("Invalid bottom string").queue()
            }
        } else {
            channel.sendMessage("Invalid usage or the channel isn't whitelisted!").queue()
        }
    }
}