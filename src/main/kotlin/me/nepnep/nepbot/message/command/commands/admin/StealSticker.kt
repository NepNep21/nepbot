package me.nepnep.nepbot.message.command.commands.admin

import me.nepnep.nepbot.isDiscord
import me.nepnep.nepbot.message.command.AbstractCommand
import me.nepnep.nepbot.message.command.Category
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.GuildMessageChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.internal.requests.Route
import net.dv8tion.jda.internal.requests.restaction.AuditableRestActionImpl
import net.dv8tion.jda.internal.utils.IOUtil
import okhttp3.MediaType
import okhttp3.MultipartBody
import java.net.URL

class StealSticker : AbstractCommand(
    "stealsticker",
    Category.ADMIN,
    "Steals a sticker: ;stealsticker (<String link> \"<String description>\" \"<CommaSeparated tags>\" \"<String name>\") | <Sticker sticker>",
    Permission.MANAGE_EMOTES_AND_STICKERS
) {
    override fun execute(args: List<String>, event: MessageReceivedEvent, channel: GuildMessageChannel) {
        val guild = event.guild

        if (!guild.selfMember.hasPermission(Permission.MANAGE_EMOTES_AND_STICKERS)) {
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
            val sticker = stickers[0]
            url = sticker.iconUrl
            name = sticker.name
            description = sticker.description
            tags = sticker.tags.joinToString().ifEmpty { " " }
        } else {
            url = args[0]

            val argsString = args.joinToString(" ") { if (it != url) it else "" }

            val regex = "\".+?\"".toRegex()
            val matches = regex.findAll(argsString)
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

        val connection = actualUrl.openConnection()
        connection.setRequestProperty("User-Agent", "") // To fix discord file downloading weirdness
        val stream = connection.inputStream

        val data = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("name", name)
            .addFormDataPart("description", description)
            .addFormDataPart("tags", tags)
            .addFormDataPart("file", "sticker", IOUtil.createRequestBody(MediaType.get(connection.contentType), stream))
            .build()
        // JDA doesn't support sticker uploading in its API yet
        AuditableRestActionImpl<Unit>(event.jda, Route.post("guilds/{guildId}/stickers").compile(guild.id), data).queue(
            null
        ) {
            channel.sendMessage(it.message!!).queue()
        }
    }
}