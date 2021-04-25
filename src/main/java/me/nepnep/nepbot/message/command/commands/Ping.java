package me.nepnep.nepbot.message.command.commands;

import me.nepnep.nepbot.message.command.Category;
import me.nepnep.nepbot.message.command.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;

import java.util.List;

public class Ping implements ICommand {
    @Override
    public void execute(List<String> args, GuildMessageReceivedEvent event) {
        long time = System.currentTimeMillis();
        TextChannel channel = event.getChannel();

        try {
            channel.sendMessage("Pong!").queue(response -> response.editMessageFormat("Pong: %d%s", System.currentTimeMillis() - time, "ms").queue());
        } catch (ErrorResponseException e) {
            // Intentionally empty
        }
    }
    @Override
    public String getInvoke() {
        return "ping";
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
        return "Pong!";
    }
}
