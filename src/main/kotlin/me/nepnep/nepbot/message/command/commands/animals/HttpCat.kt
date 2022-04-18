package me.nepnep.nepbot.message.command.commands.animals

import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.message.command.AbstractCommand
import me.nepnep.nepbot.request
import net.dv8tion.jda.api.entities.GuildMessageChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class HttpCat : AbstractCommand(
    "http",
    Category.ANIMALS,
    "Http cats: ;http <int code>"
) {
    override fun execute(args: List<String>, event: MessageReceivedEvent, channel: GuildMessageChannel) {
        if (args.isEmpty()) {
            channel.sendMessage("Invalid usage").queue()
            return
        }

        val url = "https://http.cat/${args[0]}"
        event.jda.httpClient.request(url, {
            if (it.code == 200) {
                channel.sendMessage(url).queue()
            } else {
                channel.sendMessage("https://http.cat/404").queue()
            }
        }) {
            channel.sendMessage("Request failed").queue()
        }
    }
}