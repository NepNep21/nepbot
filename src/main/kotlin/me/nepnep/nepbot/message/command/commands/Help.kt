package me.nepnep.nepbot.message.command.commands

import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.message.command.CommandRegister
import me.nepnep.nepbot.message.command.ICommand
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

class Help : ICommand {
    override fun execute(args: List<String>, event: GuildMessageReceivedEvent) {
        val channel = event.channel
        if (args.isEmpty()) {
            val categories = Category.values().joinToString("\n") { it.toString() }

            val embed = EmbedBuilder()
                .setDescription("General:\nCategories:\n\n$categories\n\nCommands:\n\n${getDescriptions(Category.GENERAL)}")
                .build()
            channel.sendMessageEmbeds(embed).queue()
            return
        }

        val page = args[0].toIntOrNull()

        if (page == null) {
            channel.sendMessage("Invalid page!").queue()
            return
        }

        if (page > Category.values().size || page < 1) {
            channel.sendMessage("Invalid page!").queue()
            return
        }

        val builder = EmbedBuilder()
        when (page) {
            Category.values().size -> {
                builder.setDescription("Communism\n\nOh my how lewd\n\nWhat if?")
            }
            Category.values().size - 1 -> {
                builder.setDescription("JoinMessage\n\nDefaultRole\n\nLeaveMessage\n\nStarboard")
            }
            else -> {
                builder.setDescription(getDescriptions(Category.values()[page - 1]))
            }
        }
        channel.sendMessageEmbeds(builder.build()).queue()
    }

    private fun getDescriptions(category: Category): String {
        val commands = CommandRegister.register.values.filter { it.getCategory() == category }

        val builder = StringBuilder()
        for (command in commands) {
            builder.append(command.getInvoke() + "\n${command.getDescription()}\n\n")
        }
        return builder.toString()
    }

    override fun getInvoke() = "help"

    override fun getCategory() = Category.GENERAL

    override fun getDescription() = "Help page: ;help <int page> | null"
}