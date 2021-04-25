package me.nepnep.nepbot.message.command.commands.fun;

import me.nepnep.nepbot.message.command.ICommand;
import me.nepnep.nepbot.message.command.Category;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class Lmddgtfy implements ICommand {
    @Override
    public void execute(List<String> args, GuildMessageReceivedEvent event) {
        StringBuilder query = new StringBuilder();
        EmbedBuilder builder = new EmbedBuilder();

        for (String arg : args) {
            query.append(" " + arg);
        }

        String queryFormatted = query.toString().replace(" ", "%20");

        builder.setDescription("[Result](https://lmddgtfy.net/?q=" + queryFormatted + ")");

        event.getChannel().sendMessage(builder.build()).queue();
    }
    @Override
    public String getInvoke() {
        return "lmddgtfy";
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
        return "Searches something for you on duck duck go: ;lmddgtfy <String query>";
    }
}
