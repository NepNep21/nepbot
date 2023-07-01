package me.nepnep.nepbot.message.command.commands.mod

import me.nepnep.nepbot.QUOTED_REGEX
import me.nepnep.nepbot.message.command.AbstractCommand
import me.nepnep.nepbot.message.command.Category
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import java.time.Duration
import java.util.concurrent.TimeUnit

class Mute : AbstractCommand(
    "mute",
    Category.MOD,
    "Mutes someone: ;mute <Mention member> <optional Time duration> \"<String reason>\"",
    Permission.MODERATE_MEMBERS
) {
    private val validRegex = "\\b\\d{1,2}[mhd]".toRegex()
    private val actualMaxTimeout = Member.MAX_TIME_OUT_LENGTH - 1

    override suspend fun execute(args: List<String>, event: MessageReceivedEvent, channel: GuildMessageChannel) {
        val mentioned = event.message.mentions.members

        if (mentioned.isEmpty() || args.size < 2) {
            channel.sendMessage(":x: Incorrect usage").queue()
            return
        }
        if (mentioned.size > 1) {
            channel.sendMessage(":x: Please only mention one user.").queue()
            return
        }
        val toMute = mentioned[0]
        val guild = event.guild

        if (!event.member!!.canInteract(toMute)) {
            channel.sendMessage(":x: You don't have permission to use this command.").queue()
            return
        }

        val selfMember = guild.selfMember
        if (!selfMember.hasPermission(Permission.MODERATE_MEMBERS) || !selfMember.canInteract(toMute)) {
            channel.sendMessage(":x: I can't timeout that user or I don't have permission to timeout.").queue()
            return
        }

        val reason = QUOTED_REGEX.find(args.joinToString(" "))?.value?.replace("\"", "")

        if (reason == null) {
            channel.sendMessage(":x: Provide a reason").queue()
            return
        }

        val timeFormat = validRegex.find(args[1])?.value

        if (timeFormat != null) {
            val last = timeFormat.last()
            val modifier = when (last) {
                'm' -> TimeUnit.MINUTES
                'h' -> TimeUnit.HOURS
                'd' -> TimeUnit.DAYS
                else -> throw IllegalStateException("Validation regex failed")
            }
            val amount = timeFormat.removeSuffix(last.toString()).toLong()
            if (last == 'd' && amount > actualMaxTimeout) {
                channel.sendMessage(":x: You can't mute members for that long").queue()
                return
            }
            toMute.timeoutFor(amount, modifier).queue()
        } else {
            toMute.timeoutFor(Duration.ofDays(actualMaxTimeout.toLong())).queue()
        }
        channel.sendMessage("${event.author.name} muted ${toMute.user.name} for reason: $reason").queue()
    }
}