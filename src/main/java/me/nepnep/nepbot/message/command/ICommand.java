package me.nepnep.nepbot.message.command;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public interface ICommand {
    void execute(List<String> args, GuildMessageReceivedEvent event);

    String getInvoke();

    Permission requiredPermission();

    Category getCategory();

    String getDescription();
}
