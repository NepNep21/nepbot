package me.nepnep.nepbot.message.command.commands.admin;

import me.nepnep.nepbot.json.BottomWhiteList;
import me.nepnep.nepbot.message.command.Category;
import me.nepnep.nepbot.message.command.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class BottomWhiteListCommand implements ICommand {
    @Override
    public void execute(List<String> args, GuildMessageReceivedEvent event) {
        BottomWhiteList.add(event.getChannel().getIdLong());
    }

    @Override
    public String getInvoke() {
        return "bottomwhitelist";
    }

    @Override
    public Permission requiredPermission() {
        return Permission.MANAGE_CHANNEL;
    }

    @Override
    public Category getCategory() {
        return Category.ADMIN;
    }

    @Override
    public String getDescription() {
        return "Adds the channel to the bottom whitelist";
    }
}
