package me.nepnep.nepbot.message.command.commands.admin;

import me.nepnep.nepbot.message.command.ICommand;
import me.nepnep.nepbot.message.command.Category;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Icon;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class Steal implements ICommand {
    @Override
    public void execute(List<String> args, GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        if (args.get(1).length() <= 32 && args.get(1).length() >= 2) {
            if (args.size() == 2) {
                try {
                    URL url = new URL(args.get(0));

                    URLConnection connection = url.openConnection();

                    connection.setRequestProperty("User-Agent", "");

                    InputStream stream = connection.getInputStream();

                    event.getGuild().createEmote(args.get(1), Icon.from(stream)).queue(s -> {}, f -> channel.sendMessage(f.getMessage()).queue());
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                } catch (InsufficientPermissionException e) {
                    channel.sendMessage("Insufficient permissions!").queue();
                }
            } else {
                channel.sendMessage("Invalid usage!").queue();
            }
        } else {
            channel.sendMessage("Name must be between (inclusive) 2 and 32 characters in length").queue();
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
