package me.nepnep.nepbot.message.command.commands.music

import me.nepnep.nepbot.message.command.AbstractCommand
import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.music.MusicManager
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class Skip : AbstractCommand(
    "skip",
    Category.MUSIC,
    "Skips the current track"
) {
    override suspend fun execute(args: List<String>, event: MessageReceivedEvent, channel: GuildMessageChannel) {
        MusicManager.players[event.guild.idLong]?.skip() ?: event.channel.sendMessage("Not currently playing!").queue()
    }
}