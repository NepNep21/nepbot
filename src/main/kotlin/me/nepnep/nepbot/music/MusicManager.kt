package me.nepnep.nepbot.music

import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers

object MusicManager {
    val audioPlayerManager = DefaultAudioPlayerManager()
    // Long is the guild
    val players = mutableMapOf<Long, TrackScheduler>()

    init {
        AudioSourceManagers.registerRemoteSources(audioPlayerManager)
    }
}