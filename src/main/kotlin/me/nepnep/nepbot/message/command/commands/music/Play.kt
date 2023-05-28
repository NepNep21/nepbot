package me.nepnep.nepbot.message.command.commands.music

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import me.nepnep.nepbot.message.command.AbstractCommand
import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.music.MusicManager
import me.nepnep.nepbot.music.TrackScheduler
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.channel.ChannelType
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class Play : AbstractCommand(
    "play",
    Category.MUSIC,
    "Play audio from youtube"
) {
    override suspend fun execute(args: List<String>, event: MessageReceivedEvent, channel: GuildMessageChannel) {
        if (args.isEmpty()) {
            channel.sendMessage("Args must not be empty").queue()
            return
        }

        val guild = event.guild
        val vc = event.member?.voiceState?.channel
        if (vc == null || vc.type ==  ChannelType.STAGE) {
            channel.sendMessage("Not in a voice channel or it is a stage channel!").queue()
            return
        }

        val vcLimit = vc.asVoiceChannel().userLimit
        if (!guild.selfMember.hasPermission(Permission.VOICE_CONNECT) || (vcLimit != 0 && vc.members.size == vcLimit)) {
            channel.sendMessage("Cannot connect").queue()
            return
        }

        val guildId = guild.idLong
        var scheduler: TrackScheduler? = MusicManager.players[guildId]
        val vcId = vc.idLong
        val textId = channel.idLong
        if (scheduler == null) {
            val player = MusicManager.audioPlayerManager.createPlayer()
            scheduler = TrackScheduler(player, textId, vcId, event.jda, guildId)
            player.addListener(scheduler)
            MusicManager.players[guildId] = scheduler
        } else {
            if (scheduler.vc != vcId) {
                channel.sendMessage("You are not in the player's voice channel!\nEither run this command there or join it and use the `switchplayer` command").queue()
                return
            }
            if (scheduler.textChannel != textId) {
                channel.sendMessage("You are not in the player's text channel!\nEither run this command there or join it and use the `switchplayer` command")
                return
            }
        }

        MusicManager.audioPlayerManager.loadItem(args[0], object : AudioLoadResultHandler {
            override fun trackLoaded(track: AudioTrack) {
                scheduler.queue(track)
            }

            override fun playlistLoaded(playlist: AudioPlaylist) {
                scheduler.queuePlaylist(playlist)
            }

            override fun noMatches() {
                channel.sendMessage("No matches found").queue()
            }

            override fun loadFailed(exception: FriendlyException) {
                if (exception.severity == FriendlyException.Severity.COMMON) {
                    channel.sendMessage(exception.message!!).queue()
                }  else {
                    throw exception
                }
            }
        })
    }
}