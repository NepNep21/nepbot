package me.nepnep.nepbot

import me.nepnep.nepbot.database.BlacklistType
import me.nepnep.nepbot.database.removeFromBlacklist
import me.nepnep.nepbot.database.removeFromWhitelist
import net.dv8tion.jda.api.events.thread.ThreadHiddenEvent
import net.dv8tion.jda.api.events.thread.ThreadRevealedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class ThreadListener : ListenerAdapter() {
    override fun onThreadRevealed(event: ThreadRevealedEvent) {
        event.thread.join().queue()
    }

    override fun onThreadHidden(event: ThreadHiddenEvent) {
        val thread = event.thread
        for (type in BlacklistType.values()) {
            thread.removeFromBlacklist(type)
        }
        thread.removeFromWhitelist()
    }
}