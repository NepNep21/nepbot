package me.nepnep.nepbot.plugins.uncalled

import dev.minn.jda.ktx.events.CoroutineEventListener
import me.nepnep.nepbot.canSend
import me.nepnep.nepbot.plugin.Plugin
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.thread.ThreadHiddenEvent

private suspend fun CoroutineEventListener.messages(event: MessageReceivedEvent) {
    if (!event.isFromGuild) {
        return
    }
    val channel = event.channel.asGuildMessageChannel()
    val shouldSendLewd = !channel.isInBlacklist(BlacklistType.LEWD)
    val content = event.message.contentRaw.lowercase()

    if (event.guild.selfMember.canSend(channel) && !event.author.isBot) {
        if (content.contains("communism") || content.contains("socialism")) {
            channel.sendMessage("https://www.youtube.com/watch?v=gnXUFXc2Yns&ab_channel=ComradeLuigi")
                .queue()
        }
        if ((content.contains("lewd")
                    || content.contains("sex")
                    || content.contains("cum")
                    || content.contains("semen")
                    || content.contains("penis")) && shouldSendLewd
        ) {
            channel.sendMessage("https://tenor.com/view/neptune-how-lewd-gif-11721711").queue()
        }
        if (content.contains("what if") && !channel.isInBlacklist(BlacklistType.TRY_IT_AND_SEE)) {
            channel.sendMessage("https://tryitands.ee/").queue()
        }
    }
}

private suspend fun CoroutineEventListener.onThreadHidden(event: ThreadHiddenEvent) {
    val thread = event.thread
    for (type in BlacklistType.values()) {
        thread.removeFromBlacklist(type)
    }
}

class UncalledPlugin : Plugin() {
    override fun onLoad() {
        addListener(CoroutineEventListener::messages)
        addListener(CoroutineEventListener::onThreadHidden)

        commands.add(LewdBlacklist())
        commands.add(RemoveLewdBlacklist())
        commands.add(TryItAndSeeBlacklist())
        super.onLoad()
    }
}