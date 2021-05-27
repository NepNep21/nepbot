package me.nepnep.nepbot;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.nepnep.nepbot.message.Messages;
import me.nepnep.nepbot.message.command.CommandRegister;
import me.nepnep.nepbot.message.command.CommandResponder;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import okhttp3.OkHttpClient;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;

public class Main {

    public static final OkHttpClient HTTP_CLIENT = new OkHttpClient();
    public static JsonNode config;

    public static void main(String[] args) throws LoginException, IOException {
        String token = System.getenv("BOT_TOKEN");
        config = new ObjectMapper().readTree(new File("config.json"));
        JsonNode activity = config.get("activity");
        String activityContent = activity.get("content").asText();

        CommandRegister.registerCommands();

        JDABuilder builder = JDABuilder.createDefault(token);

        builder.setBulkDeleteSplittingEnabled(false);

        builder.enableIntents(GatewayIntent.GUILD_MEMBERS);

        builder.setMemberCachePolicy(MemberCachePolicy.ALL);

        switch (activity.get("type").asText()) {
            case "watching":
                builder.setActivity(Activity.watching(activityContent));
                break;
            case "competing":
                builder.setActivity(Activity.competing(activityContent));
                break;
            case "listening":
                builder.setActivity(Activity.listening(activityContent));
                break;
            case "playing":
                builder.setActivity(Activity.playing(activityContent));
        }

        builder.addEventListeners(new CommandResponder(), new Messages(), new DefaultRole(), new Starboard(), new LeaveMessage(), new JoinMessage());

        builder.setMaxReconnectDelay(config.get("reconnectDelay").intValue());

        builder.build();
    }
}