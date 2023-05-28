package me.nepnep.nepbot.message.command.commands.music

import me.nepnep.nepbot.message.command.AbstractCommand
import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.music.MusicManager
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class Stop : AbstractCommand(
    "stop",
    Category.MUSIC,
    "Stops playing"
) {
    override suspend fun execute(args: List<String>, event: MessageReceivedEvent, channel: GuildMessageChannel) {
        val guildId = event.guild.idLong
        MusicManager.players[guildId]?.stop()
        MusicManager.players.remove(guildId)
    }
}