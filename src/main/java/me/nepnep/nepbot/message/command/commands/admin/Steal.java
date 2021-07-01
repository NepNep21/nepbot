package me.nepnep.nepbot.message.command.commands.admin;

import me.nepnep.nepbot.message.command.Category;
import me.nepnep.nepbot.message.command.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Icon;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class Steal implements ICommand {
    @Override
    public void execute(List<String> args, GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        if (!event.getGuild().getSelfMember().hasPermission(channel, Permission.MANAGE_EMOTES)) {
            channel.sendMessage("Insufficient bot permissions!").queue();
            return;
        }
        if (args.size() != 2) {
            channel.sendMessage("Invalid usage!").queue();
            return;
        }
        if (args.get(1).length() > 32 || args.get(1).length() < 2) {
            channel.sendMessage("Name must be between (inclusive) 2 and 32 characters in length").queue();
            return;
        }
        try {
            URL url = new URL(args.get(0));

            URLConnection connection = url.openConnection();

            connection.setRequestProperty("User-Agent", "");

            InputStream stream = connection.getInputStream();

            event.getGuild().createEmote(args.get(1), Icon.from(stream)).queue(null, f -> channel.sendMessage(f.getMessage()).queue());
        } catch (MalformedURLException e) {
            channel.sendMessage("Malformed URL").queue();
        } catch (IOException e) {
            LoggerFactory.getLogger(Steal.class).error("IOException", e);
        }
    }

    @Override
    public String getInvoke() {
        return "steal";
    }

    @Override
    public Permission requiredPermission() {
        return Permission.MANAGE_EMOTES;
    }

    @Override
    public Category getCategory() {
        return Category.ADMIN;
    }

    @Override
    public String getDescription() {
        return "Steals an emote: ;steal <String url> <String name>";
    }
}
