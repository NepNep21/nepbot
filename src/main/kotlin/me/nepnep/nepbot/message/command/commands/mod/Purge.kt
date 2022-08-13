package me.nepnep.nepbot.message.command.commands.mod

import me.nepnep.nepbot.message.command.AbstractCommand
import me.nepnep.nepbot.message.command.Category
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.GuildMessageChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import java.time.OffsetDateTime

class Purge : AbstractCommand(
    "purge",
    Category.MOD,
    "Purges messaages: ;purge <int amount> <optional Mention... members>",
    Permission.MESSAGE_MANAGE
) {
    override suspend fun execute(args: List<String>, event: MessageReceivedEvent, channel: GuildMessageChannel) {
        val mentions = event.message.mentions.members
        val doesNotMentionSomeone = mentions.isEmpty()
        // May not be required
        val selfMember = event.guild.selfMember
        if (!selfMember.hasPermission(channel, Permission.MESSAGE_HISTORY)
            || !selfMember.hasPermission(channel, Permission.MESSAGE_MANAGE)
        ) {
            channel.sendMessage("Missing permission MESSAGE_HISTORY or MESSAGE_MANAGE").queue()
            return
        }

        if (args.isEmpty() && doesNotMentionSomeone) {
            channel.sendMessage("You must enter an amount").queue()
            return
        }

        val amount = args[0].toIntOrNull()
        if (amount == null && doesNotMentionSomeone) {
            channel.sendMessage("Invalid amount").queue()
            return
        }
        val history = channel.iterableHistory

        if (amount == null) {
            var id: Long = 0
            var remaining: Int
            fun purgeJob() {
                history.skipTo(id).takeUntilAsync(1000) {
                    it.timeCreated.isBefore(OffsetDateTime.now().minusWeeks(2)) // Purging messages over 2 weeks old is very inefficient
                }.thenApply { messages -> messages.filter { it.member in mentions } }.thenAccept { messages ->
                    id = messages.last().idLong
                    remaining = messages.size

                    channel.purgeMessages(messages).forEach { it.join() }
                    if (remaining > 1) {
                        purgeJob()
                    }
                }
            }
            purgeJob()
        } else {
            history.takeUntilAsync(amount) { it.timeCreated.isBefore(OffsetDateTime.now().minusWeeks(2)) }.run {
                if (doesNotMentionSomeone) {
                    this
                } else {
                    thenApply { list -> list.filter { it.member in mentions } }
                }
            }.thenAccept(channel::purgeMessages)
        }
    }
}