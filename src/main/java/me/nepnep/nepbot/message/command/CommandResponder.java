package me.nepnep.nepbot.message.command;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class CommandResponder extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String content = event.getMessage().getContentRaw();
        Guild guild = event.getGuild();

        String prefix = ";";

        if (!event.getAuthor().isBot() && !event.getMessage().isWebhookMessage() && content.toLowerCase().startsWith(prefix.toLowerCase()) && guild.getSelfMember().hasPermission(event.getChannel(), Permission.MESSAGE_WRITE)) {

            final String[] split = event.getMessage().getContentRaw().replaceFirst(
                    "(?i)" + Pattern.quote(prefix), "").split("\\s+");
            final String invoke = split[0].toLowerCase();
            if (CommandRegister.REGISTER.containsKey(invoke)) {

                ICommand command = CommandRegister.REGISTER.get(invoke);

                guild.retrieveMember(event.getAuthor()).queue(member -> {
                    if (member.hasPermission(command.requiredPermission())) {

                        final List<String> args = Arrays.asList(split).subList(1, split.length);
                        command.execute(args, event);

                    } else {
                        event.getChannel().sendMessage("Invalid permissions").queue();
                    }
                });

            }
        }
    }
}