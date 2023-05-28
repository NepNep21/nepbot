package me.nepnep.nepbot.message.command.commands.music

import me.nepnep.nepbot.message.command.AbstractCommand
import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.music.MusicManager
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class Volume : AbstractCommand(
    "volume",
    Category.MUSIC,
    "Changes the volume: ;volume <1..100>"
) {
    override suspend fun execute(args: List<String>, event: MessageReceivedEvent, channel: GuildMessageChannel) {
        if (args.isEmpty()) {
            channel.sendMessage("Args must not be empty").queue()
            return
        }
        val volume = args[0].toIntOrNull()

        if (volume == null) {
            channel.sendMessage("Invalid Int").queue()
            return
        }

        if (volume !in 1..100) {
            channel.sendMessage("Volume must be between 1 and 100 (inclusive)").queue()
            return
        }
        MusicManager.players[event.guild.idLong]?.setVolume(volume)
    }
}