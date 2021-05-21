package me.nepnep.nepbot.message.command.commands.admin;

import me.nepnep.nepbot.database.JoinMessageDatabase;
import me.nepnep.nepbot.message.command.Category;
import me.nepnep.nepbot.message.command.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class JoinMessage implements ICommand {
    @Override
    public void execute(List<String> args, GuildMessageReceivedEvent event) {
        final long guildId = event.getGuild().getIdLong();
        if (args.size() >= 3 && args.get(0).equals("set")) {
            JoinMessageDatabase.setJoinDetails(args.get(1), String.join(" ", args.subList(2, args.size())), guildId);
        } else if (args.size() == 1 && args.get(0).equals("remove")) {
            JoinMessageDatabase.setJoinDetails("0", "0", guildId);
        }
    }

    @Override
    public String getInvoke() {
        return "joinmessage";
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
        return ";joinmessage (set <long channelId> <String message (Use %s where you want the user's name to be)>) | remove";
    }
}
