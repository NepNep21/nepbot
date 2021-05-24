package me.nepnep.nepbot;

import me.nepnep.nepbot.message.Messages;
import me.nepnep.nepbot.message.command.CommandRegister;
import me.nepnep.nepbot.message.command.CommandResponder;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import okhttp3.OkHttpClient;

import javax.security.auth.login.LoginException;

public class Main {

    public static final OkHttpClient HTTP_CLIENT = new OkHttpClient();

    public static void main(String[] args) throws LoginException {
        String token = System.getenv("BOT_TOKEN");

        CommandRegister.registerCommands();

        JDABuilder builder = JDABuilder.createDefault(token);

        builder.setBulkDeleteSplittingEnabled(false);

        builder.enableIntents(GatewayIntent.GUILD_MEMBERS);

        builder.setMemberCachePolicy(MemberCachePolicy.ALL);

        builder.setActivity(Activity.watching("Vert make a sister sandwich"));

        builder.addEventListeners(new CommandResponder(), new Messages(), new DefaultRole(), new Starboard(), new LeaveMessage(), new JoinMessage());

        builder.build();
    }
}