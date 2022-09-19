package me.nepnep.nepbot.message.command.commands.admin

import dev.minn.jda.ktx.coroutines.await
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.nepnep.nepbot.isDiscord
import me.nepnep.nepbot.message.command.AbstractCommand
import me.nepnep.nepbot.message.command.Category
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel
import net.dv8tion.jda.api.entities.Icon
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.exceptions.ErrorResponseException
import java.net.MalformedURLException
import java.net.URL

class Steal : AbstractCommand(
    "steal",
    Category.ADMIN,
    "Steals an emote: ;steal <String url> <String name>",
    Permission.MANAGE_EMOJIS_AND_STICKERS
) {
    override suspend fun execute(args: List<String>, event: MessageReceivedEvent, channel: GuildMessageChannel) {
        val guild = event.guild

        if (!guild.selfMember.hasPermission(Permission.MANAGE_EMOJIS_AND_STICKERS)) {
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

            withContext(Dispatchers.IO) {
                val connection = url.openConnection()
                connection.setRequestProperty("User-Agent", "")
                connection.inputStream.use {
                    try {
                        guild.createEmoji(args[1], Icon.from(it)).await()
                    } catch (e: ErrorResponseException) {
                        channel.sendMessage(e.message!!).queue()
                    }
                }
            }
        } catch (e: MalformedURLException) {
            channel.sendMessage("Malformed URL").queue()
        }
    }
}