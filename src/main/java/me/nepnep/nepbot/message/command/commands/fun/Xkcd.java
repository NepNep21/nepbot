package me.nepnep.nepbot.message.command.commands.fun;

import me.nepnep.nepbot.message.command.ICommand;
import me.nepnep.nepbot.message.command.Category;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class Xkcd implements ICommand {
    @Override
    public void execute(List<String> args, GuildMessageReceivedEvent event) {
        int comic = (int) (Math.random() * (2421 - 1)) + 1;
        event.getChannel().sendMessage("https://xkcd.com/" + comic).queue();
    }
    @Override
    public String getInvoke() {
        return "xkcd";
    }
    @Override
    public Permission requiredPermission() {
        return null;
    }
    @Override
    public Category getCategory() {
        return Category.FUN;
    }
    @Override
    public String getDescription() {
        return "Returns a random xkcd comic";
    }
}
