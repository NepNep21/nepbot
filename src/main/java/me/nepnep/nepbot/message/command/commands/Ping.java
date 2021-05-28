package me.nepnep.nepbot.message.command.commands;

import me.nepnep.nepbot.message.command.Category;
import me.nepnep.nepbot.message.command.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class Ping implements ICommand {
    @Override
    public void execute(List<String> args, GuildMessageReceivedEvent event) {
        event.getChannel().sendMessageFormat("Pong: %d%s", event.getJDA().getGatewayPing(), "ms").queue();
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
