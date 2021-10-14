package me.nepnep.nepbot.message.command.commands.`fun`

import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.message.command.ICommand
import me.nepnep.nepbot.request
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

class Xkcd : ICommand {
    override fun execute(args: List<String>, event: GuildMessageReceivedEvent) {
        val channel = event.channel
        when (args.size) {
            0 -> {
                event.jda.httpClient.request("https://c.xkcd.com/random/comic/", {
                    channel.sendMessage(it.request().url().toString()).queue()
                }) {
                    channel.sendMessage("Request failed").queue()
                }
            }
            1 -> {
                val url = "https://xkcd.com/" + args[0]

                event.jda.httpClient.request(url, {
                    if (it.code() == 200) {
                        channel.sendMessage(url).queue()
                    } else {
                        channel.sendMessage("Comic does not exist").queue()
                    }
                }) {
                    channel.sendMessage("Request failed").queue()
                }
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