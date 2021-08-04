package me.nepnep.nepbot.message.command.commands.animals

import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.message.command.ICommand
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class HttpCat : ICommand {
    override fun execute(args: List<String>, event: GuildMessageReceivedEvent) {
        val channel = event.channel
        if (args.isEmpty()) {
            channel.sendMessage("Invalid usage").queue()
            return
        }

        val url = "https://http.cat/${args[0]}"

        val request = Request.Builder().url(url).build()
        event.jda.httpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                channel.sendMessage("Request failed").queue()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (it.code() == 200) {
                        channel.sendMessage(url).queue()
                    } else {
                        channel.sendMessage("https://http.cat/404").queue()
                    }
                }
            }
        })
    }

    override fun getInvoke(): String {
        return "http"
    }

    override fun getCategory(): Category {
        return Category.ANIMALS
    }

    override fun getDescription(): String {
        return "Http cats: ;http <int code>"
    }
}