package me.nepnep.nepbot.message.command.commands.mod

import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.message.command.AbstractCommand
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.GuildMessageChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class Kick : AbstractCommand(
    "kick",
    Category.MOD,
    "Kicks someone: ;kick <Mention member> <String reason>",
    Permission.KICK_MEMBERS
) {
    override fun execute(args: List<String>, event: MessageReceivedEvent, channel: GuildMessageChannel) {
        val mentioned = event.message.mentionedMembers
        if (mentioned.isEmpty() || args.size < 2 || mentioned.size > 1) {
            channel.sendMessage("Failed, make sure to mention one member and specify a reason").queue()
            return
        }
        val toKick = mentioned[0]
        val reason = args.subList(1, args.size).joinToString(" ")
        val guild = event.guild
        val author = event.author

        val selfMember = guild.selfMember
        if (event.member!!.canInteract(toKick)
            && selfMember.canInteract(toKick)
            && selfMember.hasPermission(Permission.KICK_MEMBERS)
        ) {
            val authorTag = author.asTag
            val toKickUser = toKick.user
            val toKickTag = toKickUser.asTag
            channel.sendMessage("$authorTag kicked $toKickTag for reason: $reason").queue()

            if (!toKickUser.isBot) {
                toKickUser.openPrivateChannel().queue {
                    it.sendMessage("You have been kicked from $guild, by $authorTag, for reason: $reason").queue()
                }
            }
            guild.kick(toKick, reason).queue()
        }
    }
}