package me.nepnep.nepbot.message.command.commands.admin;

import me.nepnep.nepbot.json.LewdBlackList;
import me.nepnep.nepbot.message.command.ICommand;
import me.nepnep.nepbot.message.command.Category;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class RemoveLewdBlackList implements ICommand {
    @Override
    public void execute(List<String> args, GuildMessageReceivedEvent event) {
        LewdBlackList.remove(event.getChannel().getIdLong());
    }
    @Override
    public String getInvoke() {
        return "removelewdblacklist";
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
        return "Removes the current channel from the lewd blacklist";
    }
}