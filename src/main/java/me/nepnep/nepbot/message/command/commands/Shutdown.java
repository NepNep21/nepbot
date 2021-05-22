package me.nepnep.nepbot.message.command.commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.nepnep.nepbot.message.command.Category;
import me.nepnep.nepbot.message.command.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Shutdown implements ICommand {
    @Override
    public void execute(List<String> args, GuildMessageReceivedEvent event) {
        try {
            long operator = new ObjectMapper().readTree(new File("config.json")).get("operator").longValue();
            TextChannel channel = event.getChannel();
            if (event.getAuthor().getIdLong() == operator) {
                channel.sendMessage("Shutting down").complete();
                event.getJDA().shutdown();
                System.exit(0);
            } else {
                channel.sendMessage("You do not have permission to use this command").queue();
            }
        } catch (IOException e) {
            LoggerFactory.getLogger(Shutdown.class).error("IOException at Shutdown", e);
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
