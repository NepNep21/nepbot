package me.nepnep.nepbot.message;

import me.nepnep.nepbot.Main;
import me.nepnep.nepbot.database.BlackListDatabase;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Messages extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (Main.config.get("uncalledMessages").booleanValue() && !(event.isWebhookMessage() || event.getMember().getUser().isBot())) {
            boolean shouldSendLewd = !BlackListDatabase.isInBlackList(event.getChannel().getIdLong());
            TextChannel channel = event.getChannel();
            String content = event.getMessage().getContentRaw().toLowerCase();
            Member selfMember = event.getGuild().getSelfMember();

            if (content.contains("nigger") && selfMember.hasPermission(channel, Permission.MESSAGE_MANAGE)) {
                event.getMessage().delete().queue();
            }

            if (selfMember.hasPermission(channel, Permission.MESSAGE_WRITE)) {
                if (content.contains("communism") || content.contains("socialism")) {
                    channel.sendMessage("https://www.youtube.com/watch?v=gnXUFXc2Yns&ab_channel=ComradeLuigi").queue();
                }
                if ((content.contains("lewd") || content.contains("sex") || content.contains("cum") || content.contains("semen") || content.contains("penis")) && shouldSendLewd) {
                    channel.sendMessage("https://tenor.com/view/neptunia-gif-18952040").queue();
                }
                if (content.contains("what if")) {
                    channel.sendMessage("https://tryitands.ee/").queue();
                }
            }
        }
    }
}
