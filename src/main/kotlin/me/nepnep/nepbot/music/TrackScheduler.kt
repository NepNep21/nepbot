package me.nepnep.nepbot.music

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason
import net.dv8tion.jda.api.JDA
import java.util.concurrent.LinkedBlockingQueue

class TrackScheduler(
    private val player: AudioPlayer,
    var textChannel: Long,
    var vc: Long,
    private val jda: JDA,
    private val guild: Long
) : AudioEventAdapter() {
    private val queue = LinkedBlockingQueue<AudioTrack>(20)
    private var isStarted = false

    private val fullMessage = "The queue is full (20)"
    private val queueMessage = "Adding to queue"

    private fun start() {
        if (!isStarted) {
            val voiceChannel = getVoiceChannel() ?: return
            val audioManager = getManager() ?: return
            audioManager.openAudioConnection(voiceChannel)
            audioManager.sendingHandler = AudioPlayerSendHandler(player)
            isStarted = true
        }
    }

    private fun sendMessage(message: String) {
        jda.getTextChannelById(textChannel)?.sendMessage(message)?.queue()
    }

    fun queue(track: AudioTrack) {
        start()
        if (!player.startTrack(track, true)) {
            sendMessage(queueMessage)
            if (!queue.offer(track)) {
                sendMessage(fullMessage)
            }
        }
    }

    fun getVoiceChannel() = jda.getVoiceChannelById(vc)
    
    fun queuePlaylist(playlist: AudioPlaylist) {
        start()
        var full = false
        for (track in playlist.tracks) {
            if (!player.startTrack(track, true)) {
                if (!queue.offer(track)) {
                    full = true
                    break
                }
            }
        }
        if (full) {
            sendMessage(fullMessage)
        }
    }

    private fun disconnect() {
        getManager()?.closeAudioConnection()
        queue.clear()
        isStarted = false
    }
    
    private fun getManager() = jda.getGuildById(guild)?.audioManager 

    fun stop() {
        disconnect()
        player.stopTrack()
    }
    
    fun switch(vc: Long, text: Long) {
        player.isPaused = true
        this.vc = vc
        this.textChannel = text
        val manager = getManager()!!
        manager.openAudioConnection(jda.getVoiceChannelById(vc))
        player.isPaused = false
    }

    fun skip() {
        sendMessage("Skipping")
        val next = queue.poll()
        player.startTrack(next, false)
        if (next == null) {
            disconnect()
        }
    }

    fun setVolume(volume: Int) {
        player.volume = volume
    }

    override fun onTrackEnd(player: AudioPlayer, track: AudioTrack, endReason: AudioTrackEndReason) {
        if (endReason.mayStartNext) {
            val next = queue.poll()
            player.startTrack(next, false)
            if (next == null) {
                disconnect()
            }
        }
    }

    override fun onTrackStart(player: AudioPlayer, track: AudioTrack) {
        val info = track.info
        sendMessage("Now playing ${info.title} at ${info.uri}")
    }
}