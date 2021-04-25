package me.nepnep.nepbot.message.command.commands;

import me.nepnep.nepbot.message.command.Category;
import me.nepnep.nepbot.message.command.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class Shutdown implements ICommand {
    @Override
    public void execute(List<String> args, GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        if (event.getAuthor().getIdLong() == 264171415614259200L) {
            channel.sendMessage("Shutting down").complete();
            event.getJDA().shutdown();
            // Try-catch and sleep may not be necessary
            try {
                Thread.sleep(2000);
                System.exit(0);
            } catch (InterruptedException e) {
                // Intentionally empty
            }
        } else {
            channel.sendMessage("You do not have permission to shut the bot down!").queue();
        }
    }

    @Override
    public String getInvoke() {
        return "shutdown";
    }

    @Override
    public Permission requiredPermission() {
        return null;
    }

    @Override
    public Category getCategory() {
        return Category.GENERAL;
    }

    @Override
    public String getDescription() {
        return "Shuts down the bot";
    }
}
