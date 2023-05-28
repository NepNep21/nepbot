package me.nepnep.nepbot.message.command.commands.music

import me.nepnep.nepbot.message.command.AbstractCommand
import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.music.MusicManager
import net.dv8tion.jda.api.entities.channel.ChannelType
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class SwitchPlayer : AbstractCommand(
    "switchplayer",
    Category.MUSIC,
    "Use other channels for playing music"
) {
    override suspend fun execute(args: List<String>, event: MessageReceivedEvent, channel: GuildMessageChannel) {
        val guild = event.guild
        val player = MusicManager.players[guild.idLong]
        if (player == null) {
            channel.sendMessage("No registered player for this guild (hint: run `play`)").queue()
            return
        }
        
        val vc = event.member?.voiceState!!.channel?.let { 
            if (it.type == ChannelType.VOICE) it.asVoiceChannel() else null
        }?.idLong ?: -1
        if (vc != player.vc) {
            channel.sendMessage(
                "You must be in the registered voice channel (" + 
                        guild.getVoiceChannelById(player.vc)?.asMention +
                        ") to use this command"
            ).queue()
            return
        }
        
        val target = event.message.mentions.channels.firstOrNull {
            it.type == ChannelType.VOICE
        }
        if (target == null) {
            channel.sendMessage("You must mention the voice channel you intend to use!").queue()
            return
        }
        player.switch(target.idLong, channel.idLong)
        channel.sendMessage("Set the player text channel to this and the voice channel to ${target.asMention}").queue()
    }
}