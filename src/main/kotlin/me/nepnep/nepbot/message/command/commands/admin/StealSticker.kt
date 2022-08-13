package me.nepnep.nepbot.message.command.commands.admin

import dev.minn.jda.ktx.coroutines.await
import me.nepnep.nepbot.QUOTED_REGEX
import me.nepnep.nepbot.isDiscord
import me.nepnep.nepbot.message.command.AbstractCommand
import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.runIO
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.GuildMessageChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.exceptions.ErrorResponseException
import net.dv8tion.jda.api.utils.FileUpload
import java.net.URL

class StealSticker : AbstractCommand(
    "stealsticker",
    Category.ADMIN,
    "Steals a sticker: ;stealsticker (<String link> \"<String description>\" \"<CommaSeparated tags>\" \"<String name>\") | <Sticker sticker>",
    Permission.MANAGE_EMOJIS_AND_STICKERS
) {
    override suspend fun execute(args: List<String>, event: MessageReceivedEvent, channel: GuildMessageChannel) {
        val guild = event.guild

        if (!guild.selfMember.hasPermission(Permission.MANAGE_EMOJIS_AND_STICKERS)) {
            channel.sendMessage("I can't upload stickers!").queue()
            return
        }

        val url: String
        val name: String
        val description: String
        val tags: String

        val stickers = event.message.stickers
        val hasStickers = stickers.isNotEmpty()
        if (!hasStickers && args.size < 4) {
            channel.sendMessage("Invalid usage!").queue()
            return
        }
        if (hasStickers) {
            val sticker = event.jda.retrieveSticker(stickers.first()).await()
            url = sticker.iconUrl
            name = sticker.name
            description = sticker.description
            tags = sticker.tags.joinToString().ifEmpty { " " }
        } else {
            url = args[0]

            val argsString = args.joinToString(" ") { if (it != url) it else "" }

            val matches = QUOTED_REGEX.findAll(argsString)
                .map { it.value.replace("\"", "") }
                .toList()
            description = matches.getOrNull(0) ?: return

            // May never be empty, look into this
            if (description.isNotEmpty() && description.length !in 2..100) {
                channel.sendMessage("Description must be empty OR between (inclusive) 2 and 100 characters").queue()
                return
            }

            tags = matches.getOrNull(1)?.trim() ?: return

            if (tags.length > 200) {
                channel.sendMessage("Tags must be at most 200 characters").queue()
                return
            }

            name = matches.getOrNull(2) ?: return

            if (name.length !in 2..30) {
                channel.sendMessage("Name must be between (inclusive) 2 and 30 characters").queue()
                return
            }
        }

        val actualUrl = URL(url)

        if (!actualUrl.isDiscord()) {
            channel.sendMessage("Only discord URLs may be used").queue()
            return
        }

        runIO {
            val connection = actualUrl.openConnection()
            connection.setRequestProperty("User-Agent", "") // To fix discord file downloading weirdness
            connection.inputStream.use {
                try {
                    guild.createSticker(name, description, FileUpload.fromData(it, "sticker"), tags.split(',')).await()
                } catch (e: ErrorResponseException) {
                    channel.sendMessage(e.message!!).queue()
                }
            }
        }
    }
}