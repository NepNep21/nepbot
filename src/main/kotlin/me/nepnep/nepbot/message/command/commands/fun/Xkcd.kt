package me.nepnep.nepbot.message.command.commands.`fun`

import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.message.command.ICommand
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class Xkcd : ICommand {
    override fun execute(args: List<String>, event: GuildMessageReceivedEvent) {
        val channel = event.channel
        when (args.size) {
            0 -> {
                val request = Request.Builder().url("https://c.xkcd.com/random/comic/").build()
                event.jda.httpClient.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        channel.sendMessage("Request failed").queue()
                    }

                    override fun onResponse(call: Call, response: Response) {
                        response.use {
                            channel.sendMessage(it.request().url().toString()).queue()
                        }
                    }
                })
            }
            1 -> {
                val url = "https://xkcd.com/" + args[0]

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
                                channel.sendMessage("Comic does not exist").queue()
                            }
                        }
                    }
                })
            }
            else -> {
                channel.sendMessage("Invalid usage").queue()
            }
        }
    }

    override fun getInvoke() = "xkcd"

    override fun getCategory() = Category.FUN

    override fun getDescription() = "Returns a random xkcd comic or a specific one"
}