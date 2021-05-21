package me.nepnep.nepbot;

import me.nepnep.nepbot.database.JoinMessageDatabase;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Map;

public class JoinMessage extends ListenerAdapter {
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        try {
            Guild guild = event.getGuild();

            Map<String, String> map = JoinMessageDatabase.getJoinDetails(guild.getIdLong());
            if (map != null) {
                String channelId = map.get("channel");

                TextChannel channel = guild.getTextChannelById(channelId);
                if (channel != null) {
                    String message = map.get("message");
                    channel.sendMessage(message.replace("%s", event.getUser().getAsMention())).queue();
                }
            }
        } catch (InsufficientPermissionException e) {
            // Intentionally empty
        }
    }
}
