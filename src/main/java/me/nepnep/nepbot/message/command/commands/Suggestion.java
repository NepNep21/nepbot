package me.nepnep.nepbot.message.command.commands;

import me.nepnep.nepbot.message.command.Category;
import me.nepnep.nepbot.message.command.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class Suggestion implements ICommand {
    @Override
    public void execute(List<String> args, GuildMessageReceivedEvent event) {

        TextChannel channel = event.getChannel();

        if (channel.getName().equals("suggestions")) {
            if (args.size() >= 1) {
                String suggestion = String.join(" ", args.subList(0, args.size()));
                User author = event.getAuthor();
                EmbedBuilder builder = new EmbedBuilder();

                builder.setTitle(author.getAsTag());

                builder.setThumbnail(author.getAvatarUrl());

                builder.setDescription(suggestion);

                builder.setFooter("ID: " + author.getId());

                channel.sendMessage(builder.build()).queue(message -> {
                    message.addReaction("✅").queue();
                    message.addReaction("❌").queue();
                });
                event.getMessage().delete().queue();
            } else {
                channel.sendMessage("Invalid usage!").queue();
            }
        } else {
            channel.sendMessage("Channel not named `suggestions`!").queue();
        }
    }
    @Override
    public String getInvoke() {
        return "suggestion";
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
        return "Makes a suggestion: ;suggestion <String suggestion>";
    }
}
