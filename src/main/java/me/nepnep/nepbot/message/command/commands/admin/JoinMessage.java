package me.nepnep.nepbot.message.command.commands.admin;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import me.nepnep.nepbot.JoinMessage.JSONObject;
import me.nepnep.nepbot.message.command.Category;
import me.nepnep.nepbot.message.command.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JoinMessage implements ICommand {
    @Override
    public void execute(List<String> args, GuildMessageReceivedEvent event) {
        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        Guild guild = event.getGuild();
        TextChannel channel = event.getChannel();
        String path = "joinMessage.json";

        try {
            Map<String, JSONObject> objects = mapper.readValue(new File(path), new TypeReference<Map<String, JSONObject>>() {});
            String guildId = guild.getId();

            if (args.size() >= 3 && args.get(0).equals("set")) {
                try {
                    String channelString = args.get(1);
                    TextChannel channelCheck = guild.getTextChannelById(channelString);
                    String message = String.join(" ", args.subList(2, args.size()));

                    if (channelCheck != null) {
                        objects.put(guildId, new JSONObject(Long.parseLong(channelString), message));
                        mapper.writeValue(new File(path), objects);
                    } else {
                        channel.sendMessage("Invalid usage!").queue();
                    }
                } catch (NumberFormatException e) {
                    channel.sendMessage("Invalid Usage!").queue();
                }
            } else if (args.size() == 1 && args.get(0).equals("remove") && objects.containsKey(guildId)) {
                objects.remove(guildId);
                mapper.writeValue(new File(path), objects);
            }
        } catch (IOException e) {
            LoggerFactory.getLogger(JoinMessage.class).error("IOException at JoinMessageCommand", e);
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
