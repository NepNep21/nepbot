package me.nepnep.nepbot.message.command.commands.info

import com.fasterxml.jackson.databind.ObjectMapper
import dev.minn.jda.ktx.messages.Embed
import me.nepnep.nepbot.message.command.AbstractCommand
import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.request
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import java.net.URLEncoder

class UrbanDictionary : AbstractCommand(
    "urban",
    Category.INFO,
    "Get a thing's definition from urban dictionary! ;urban <query>"
) {
    override suspend fun execute(args: List<String>, event: MessageReceivedEvent, channel: GuildMessageChannel) {
        if (args.isEmpty()) {
            channel.sendMessage("Invalid usage").queue()
            return
        }
        val api = "https://api.urbandictionary.com/v0/define?term="
        val query = URLEncoder.encode(args.subList(0, args.size).joinToString(" "), "UTF-8") // Wtf kotlin

        event.jda.httpClient.request(api + query, {
            val json = ObjectMapper().readTree(it.body!!.string())

            val list = json["list"]
            if (list == null) {
                channel.sendMessage("Not found or unknown error").queue()
                return@request
            }

            val best = list[0]
            if (best == null) {
                channel.sendMessage("Not found").queue()
                return@request
            }

            val definition = best["definition"].textValue()
            val example = best["example"].textValue()

            if (definition.length > MessageEmbed.VALUE_MAX_LENGTH || example.length > MessageEmbed.VALUE_MAX_LENGTH) {
                channel.sendMessage("Embed field is too large!").queue()
                return@request
            }

            val embed = Embed { 
                field("Definition:", definition, false)
                field("Example:", example, false)
            }

            if (embed.isSendable) {
                channel.sendMessageEmbeds(embed).queue()
                return@request
            }
            channel.sendMessage("Embed is too large!").queue()
        }) {
            channel.sendMessage("API Issues, try again later.").queue()
        }
    }
}