package me.nepnep.nepbot;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class JoinMessage extends ListenerAdapter {
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        try {
            Guild guild = event.getGuild();
            ObjectMapper mapper = new ObjectMapper();

            Map<String, JSONObject> objects = mapper.readValue(new File("joinMessage.json"), new TypeReference<Map<String, JSONObject>>() {});
            JSONObject guildObject = objects.get(event.getGuild().getId());

            long channelId = guildObject.getChannel();
            String message = guildObject.getMessage();

            TextChannel channel = guild.getTextChannelById(channelId);
            channel.sendMessage(message.replace("%s", event.getUser().getAsMention())).queue();
        } catch (IOException e) {
            LoggerFactory.getLogger(JoinMessage.class).error("Exception at JoinMessage", e);
        } catch (NullPointerException | InsufficientPermissionException e) {
            // Intentionally empty
        }
    }
    public static class JSONObject {
        private long channel;
        private String message;

        public JSONObject(long channel, String message) {
            this.channel = channel;
            this.message = message;
        }

        public JSONObject() {
            // Empty
        }

        public long getChannel() {
            return channel;
        }

        public String getMessage() {
            return message;
        }
    }
}
