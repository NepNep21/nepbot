package me.nepnep.nepbot.message.command.commands.info

import dev.minn.jda.ktx.messages.Embed
import me.nepnep.nepbot.message.command.AbstractCommand
import me.nepnep.nepbot.message.command.Category
import net.dv8tion.jda.api.JDAInfo
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import java.lang.management.ManagementFactory

class BotInfo : AbstractCommand(
    "botinfo",
    Category.INFO,
    "Shows information about the bot!"
) {
    override suspend fun execute(args: List<String>, event: MessageReceivedEvent, channel: GuildMessageChannel) {
        val guilds = event.jda.guildCache
        val runtime = Runtime.getRuntime()

        val javaVersion = System.getProperty("java.version")
        val guildAmount = guilds.size()
        val userAmount = guilds.sumOf { it.memberCount }

        val memoryUsed = runtime.totalMemory() - runtime.freeMemory()
        val uptime = ManagementFactory.getRuntimeMXBean().uptime
        val jdaVersion = JDAInfo.VERSION

        val embed = Embed { 
            field("Java Version", javaVersion)
            field("Total Guilds", guildAmount.toString())
            field("Total Users", userAmount.toString())
            field("RAM Usage", (memoryUsed / 1000000).toString() + "MB")
            field("Uptime", (uptime / 3600000).toString() + " Hours")
            field("JDA Version", jdaVersion)
        }
        channel.sendMessageEmbeds(embed).queue()
    }
}