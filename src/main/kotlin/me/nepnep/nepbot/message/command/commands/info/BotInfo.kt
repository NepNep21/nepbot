package me.nepnep.nepbot.message.command.commands.info

import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.message.command.AbstractCommand
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.JDAInfo
import net.dv8tion.jda.api.entities.GuildMessageChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import java.lang.management.ManagementFactory

class BotInfo : AbstractCommand(
    "botinfo",
    Category.INFO,
    "Shows information about the bot!"
) {
    override fun execute(args: List<String>, event: MessageReceivedEvent, channel: GuildMessageChannel) {
        val guilds = event.jda.guildCache
        val runtime = Runtime.getRuntime()

        val javaVersion = System.getProperty("java.version")
        val guildAmount = guilds.size()
        val userAmount = guilds.sumOf { it.memberCount }

        val memoryUsed = runtime.totalMemory() - runtime.freeMemory()
        val uptime = ManagementFactory.getRuntimeMXBean().uptime
        val jdaVersion = JDAInfo.VERSION

        val embed = EmbedBuilder()
            .addField("Java Version", javaVersion, true)
            .addField("Total Guilds", guildAmount.toString(), true)
            .addField("Total Users", userAmount.toString(), true)
            .addField("RAM Usage", (memoryUsed / 1000000).toString() + "MB", true)
            .addField("Uptime", (uptime / 3600000).toString() + " Hours", true)
            .addField("JDA Version", jdaVersion, true)
            .build()
        channel.sendMessageEmbeds(embed).queue()
    }
}