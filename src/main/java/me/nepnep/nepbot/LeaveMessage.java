package me.nepnep.nepbot;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.LoggerFactory;

import java.util.List;

public class LeaveMessage extends ListenerAdapter {
    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
        List<TextChannel> goodbyeChannels = event.getGuild().getTextChannelsByName("goodbye", false);

        if (goodbyeChannels.size() >= 1) {
            try {
                goodbyeChannels.get(0).sendMessage(String.format("%s Just left the server, goodbye.", event.getUser().getAsTag())).queue();
            } catch (InsufficientPermissionException e) {
                LoggerFactory.getLogger(LeaveMessage.class).debug("Attempted to send leave message but did not have the permissions!");
            }
        }
    }
}
