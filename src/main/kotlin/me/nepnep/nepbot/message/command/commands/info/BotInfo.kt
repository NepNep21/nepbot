package me.nepnep.nepbot.message.command.commands.info

import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.message.command.ICommand
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.JDAInfo
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import java.lang.management.ManagementFactory

class BotInfo : ICommand {
    override fun execute(args: List<String>, event: GuildMessageReceivedEvent) {
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
            .addField("Uptime", (uptime / 1000 / 60).toString() + " Minutes", true)
            .addField("JDA Version", jdaVersion, true)
            .build()
        event.channel.sendMessageEmbeds(embed).queue()
    }

    override fun getInvoke(): String {
        return "botinfo"
    }

    override fun getCategory(): Category {
        return Category.INFO
    }

    override fun getDescription(): String {
        return "Shows information about the bot!"
    }
}