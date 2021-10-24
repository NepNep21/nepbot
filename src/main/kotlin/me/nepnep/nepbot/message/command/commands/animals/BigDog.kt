package me.nepnep.nepbot.message.command.commands.animals

import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.message.command.AbstractCommand
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

class BigDog : AbstractCommand(
    "bigdog",
    Category.ANIMALS,
    "A big dog"
) {
    override fun execute(args: List<String>, event: GuildMessageReceivedEvent) {
        event.channel.sendMessage("https://bigdog.monster/").queue()
    }
}