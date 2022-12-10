package me.nepnep.nepbot.plugins.uwurandom

import me.nepnep.nepbot.message.command.AbstractCommand
import me.nepnep.nepbot.message.command.Category
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import java.io.File
import java.nio.CharBuffer

class UwURandom : AbstractCommand(
    "uwurandom",
    Category.FUN,
    "UwU: ;uwurandom <character count>"
) {
    override suspend fun execute(args: List<String>, event: MessageReceivedEvent, channel: GuildMessageChannel) {
        if (args.isEmpty()) {
            channel.sendMessage("Specify a character count").queue()
            return
        }
        val count = args[0].toIntOrNull()
        if (count == null) {
            channel.sendMessage("Invalid count").queue()
            return
        }
        
        val text = File("/dev/uwurandom").reader().use { 
            val buf = CharBuffer.allocate(count)
            it.read(buf)
            buf.flip()
            return@use if (buf.length > 2000) {
                null
            } else {
                buf
            }
        }
        
        if (text == null) {
            channel.sendMessage("Too long!").queue()
            return
        }
        
        channel.sendMessage(text).queue()
    }
}