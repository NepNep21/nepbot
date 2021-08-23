package me.nepnep.nepbot.message.command.commands.mod

import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.message.command.ICommand
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

class Ban : ICommand {
    override fun execute(args: List<String>, event: GuildMessageReceivedEvent) {
        val mentioned = event.message.mentionedMembers
        val channel = event.channel
        if (mentioned.isEmpty() || args.size < 2 || mentioned.size > 1) {
            channel.sendMessage("Failed, make sure to mention one member and specify a reason").queue()
            return
        }
        val toBan = mentioned[0]
        val reason = args.subList(1, args.size).joinToString(" ")
        val guild = event.guild
        val author = event.author

        val selfMember = guild.selfMember
        if (event.member!!.canInteract(toBan)
            && selfMember.canInteract(toBan)
            && selfMember.hasPermission(Permission.BAN_MEMBERS)
        ) {
            val authorTag = author.asTag
            val toBanUser = toBan.user
            val toBanTag = toBanUser.asTag
            channel.sendMessage("$authorTag banned $toBanTag for reason: $reason").queue()

            if (!toBanUser.isBot) {
                toBanUser.openPrivateChannel().queue {
                    it.sendMessage("You have been banned from $guild, by $authorTag, reason: $reason").queue()
                }
            }
            guild.ban(toBan, 0, reason).queue()
        }
    }

    override fun getInvoke() = "ban"

    override fun getRequiredPermission() = Permission.BAN_MEMBERS

    override fun getCategory() = Category.MOD

    override fun getDescription() = "Bans someone: ;ban <Mention member> <String reason>"
}