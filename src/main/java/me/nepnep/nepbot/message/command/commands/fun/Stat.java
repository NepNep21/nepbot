package me.nepnep.nepbot.message.command.commands.fun;

import me.nepnep.nepbot.Networking;
import me.nepnep.nepbot.message.command.ICommand;
import me.nepnep.nepbot.message.command.Category;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class Stat implements ICommand {
    @Override
    public void execute(List<String> args, GuildMessageReceivedEvent event) {
        event.getChannel().sendMessage(String.valueOf(Networking.pingServer(args.get(0)))).queue();
    }
    @Override
    public String getInvoke() {
        return "stat";
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
        return "Pings a URL and returns the response code: <String url>";
    }
}
