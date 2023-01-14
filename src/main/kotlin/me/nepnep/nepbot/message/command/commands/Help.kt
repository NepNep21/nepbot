package me.nepnep.nepbot.message.command.commands

import dev.minn.jda.ktx.events.onStringSelect
import dev.minn.jda.ktx.interactions.components.StringSelectMenu
import dev.minn.jda.ktx.interactions.components.option
import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.editMessage_
import dev.minn.jda.ktx.messages.send
import kotlinx.coroutines.delay
import me.nepnep.nepbot.message.command.AbstractCommand
import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.message.command.CommandRegister
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.interactions.components.ActionRow
import java.util.*
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class Help : AbstractCommand(
    "help",
    Category.GENERAL,
    "Help page"
) {
    override suspend fun execute(args: List<String>, event: MessageReceivedEvent, channel: GuildMessageChannel) {
        val id = "help:${UUID.randomUUID()}"
        val menu = createMenu(id, Category.GENERAL)
        
        event.channel.send(embeds = listOf(Embed(getDescriptions(Category.GENERAL))), components = listOf(ActionRow.of(menu))).queue()
        
        val handler = event.jda.onStringSelect(id) {
            val category = Category.valueOf(it.values[0])
            it.editMessage_(
                embeds = listOf(Embed(getDescriptions(category))),
                components = listOf(ActionRow.of(createMenu(id, category)))
            ).queue()
        }
        
        delay(1.toDuration(DurationUnit.MINUTES))
        handler.cancel()
    }

    private fun getDescriptions(category: Category): String {
        if (category == Category.EVENTS) {
            return """
                JoinMessage

                DefaultRole

                LeaveMessage

                Starboard
                
                (Potentially more in plugins)
            """
        }
        
        val commands = CommandRegister.register.values.filter { it.category == category }

        val builder = StringBuilder()
        for (command in commands) {
            builder.append(command.invoke + "\n${command.description}\n\n")
        }
        return builder.toString()
    }
    
    private fun createMenu(id: String, default: Category) = StringSelectMenu(id) {
        Category.values().forEach {
            option(it.toString(), it.name, default = default == it)
        }
    }
}