package me.nepnep.nepbot.message.command.commands.mod

import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.message.command.ICommand
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

class Purge : ICommand {
    override fun execute(args: List<String>, event: GuildMessageReceivedEvent) {
        val channel = event.channel
        // May not be required
        val selfMember = event.guild.selfMember
        if (!selfMember.hasPermission(channel, Permission.MESSAGE_HISTORY)
            || !selfMember.hasPermission(channel, Permission.MESSAGE_MANAGE)
        ) {
            channel.sendMessage("Missing permission MESSAGE_HISTORY or MESSAGE_MANAGE").queue()
        }
        val amount = args[0].toIntOrNull()
        if (amount == null) {
            channel.sendMessage("Invalid amount").queue()
            return
        }
        channel.iterableHistory.takeAsync(amount).thenAccept(channel::purgeMessages)
    }

    override fun getInvoke() = "purge"

    override fun getRequiredPermission() = Permission.MESSAGE_MANAGE

    override fun getCategory() = Category.MOD

    override fun getDescription() = "Purges messaages: ;purge <int amount>"
}