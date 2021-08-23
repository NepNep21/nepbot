package me.nepnep.nepbot.message.command.commands.info

import com.fasterxml.jackson.databind.ObjectMapper
import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.message.command.ICommand
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.net.URLEncoder

class UrbanDictionary : ICommand {
    override fun execute(args: List<String>, event: GuildMessageReceivedEvent) {
        val channel = event.channel
        if (args.isEmpty()) {
            channel.sendMessage("Invalid usage").queue()
            return
        }
        val api = "https://api.urbandictionary.com/v0/define?term="
        val query = URLEncoder.encode(args.subList(0, args.size).joinToString(" "), "UTF-8")

        val request = Request.Builder().url(api + query).build()
        event.jda.httpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                channel.sendMessage("API Issues, try again later.").queue()
            }

            override fun onResponse(call: Call, response: Response) {
                val json = ObjectMapper().readTree(response.body()!!.string())

                val list = json["list"]
                if (list == null) {
                    channel.sendMessage("Not found or unknown error").queue()
                    return
                }

                val best = list[0]
                if (best == null) {
                    channel.sendMessage("Not found").queue()
                    return
                }

                val definition = best["definition"].textValue()
                val example = best["example"].textValue()

                val embed = EmbedBuilder()
                    .addField("Definition:", definition, false)
                    .addField("Example:", example, false)
                    .build()

                if (embed.isSendable) {
                    channel.sendMessageEmbeds(embed).queue()
                    return
                }
                channel.sendMessage("Embed is too large!").queue()
            }
        })
    }

    override fun getInvoke() = "urban"

    override fun getCategory() = Category.INFO

    override fun getDescription() = "Get a thing's definition from urban dictionary! ;urban <query>"
}