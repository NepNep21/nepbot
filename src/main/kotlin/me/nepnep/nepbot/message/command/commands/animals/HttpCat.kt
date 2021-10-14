package me.nepnep.nepbot.message.command.commands.animals

import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.message.command.ICommand
import me.nepnep.nepbot.request
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

class HttpCat : ICommand {
    override fun execute(args: List<String>, event: GuildMessageReceivedEvent) {
        val channel = event.channel
        if (args.isEmpty()) {
            channel.sendMessage("Invalid usage").queue()
            return
        }

        val url = "https://http.cat/${args[0]}"
        event.jda.httpClient.request(url, {
            if (it.code() == 200) {
                channel.sendMessage(url).queue()
            } else {
                channel.sendMessage("https://http.cat/404").queue()
            }
        }) {
            channel.sendMessage("Request failed").queue()
        }
    }

    override fun getInvoke() = "http"

    override fun getCategory() = Category.ANIMALS

    override fun getDescription() = "Http cats: ;http <int code>"
}