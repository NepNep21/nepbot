package me.nepnep.nepbot.message.command.commands;

import me.nepnep.nepbot.Main;
import me.nepnep.nepbot.message.command.Category;
import me.nepnep.nepbot.message.command.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class Shutdown implements ICommand {
    @Override
    public void execute(List<String> args, GuildMessageReceivedEvent event) {
        long operator = Main.config.get("operator").longValue();
        TextChannel channel = event.getChannel();
        if (event.getAuthor().getIdLong() == operator) {
            channel.sendMessage("Shutting down").complete();
            event.getJDA().shutdown();
            System.exit(0);
        } else {
            channel.sendMessage("You do not have permission to use this command").queue();
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
