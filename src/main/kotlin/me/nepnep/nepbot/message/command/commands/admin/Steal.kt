package me.nepnep.nepbot.message.command.commands.admin

import me.nepnep.nepbot.isDiscord
import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.message.command.AbstractCommand
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Icon
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import java.net.MalformedURLException
import java.net.URL

class Steal : AbstractCommand(
    "steal",
    Category.ADMIN,
    "Steals an emote: ;steal <String url> <String name>",
    Permission.MANAGE_EMOTES
) {
    override fun execute(args: List<String>, event: GuildMessageReceivedEvent) {
        val channel = event.channel
        val guild = event.guild

        if (!guild.selfMember.hasPermission(Permission.MANAGE_EMOTES)) {
            channel.sendMessage("Insufficient bot permissions!").queue()
            return
        }
        if (args.size != 2) {
            channel.sendMessage("Invalid usage!").queue()
            return
        }
        if (args[1].length > 32 || args[1].length < 2) {
            channel.sendMessage("Name must be between (inclusive) 2 and 32 characters in length").queue()
            return
        }

        try {
            val url = URL(args[0])

            if (!url.isDiscord()) {
                channel.sendMessage("Only discord URLs may be used").queue()
                return
            }

            val connection = url.openConnection()
            connection.setRequestProperty("User-Agent", "")

            val stream = connection.getInputStream()

            guild.createEmote(args[1], Icon.from(stream)).queue(null) {
                channel.sendMessage(it.message!!).queue()
            }
        } catch (e: MalformedURLException) {
            channel.sendMessage("Malformed URL").queue()
        }
    }
}