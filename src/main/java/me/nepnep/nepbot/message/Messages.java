package me.nepnep.nepbot.message;

import me.nepnep.nepbot.database.BlackListDatabase;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Messages extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        boolean shouldSend = !BlackListDatabase.isInBlackList(event.getChannel().getIdLong());
        TextChannel channel = event.getChannel();
        String content = event.getMessage().getContentRaw().toLowerCase();

        if (!event.getAuthor().isBot() && event.getGuild().getSelfMember().hasPermission(channel, Permission.MESSAGE_WRITE)) {

            if (content.contains("nigger")) {
                event.getMessage().delete().queue();
            }
            if (content.contains("communism") || content.contains("socialism")) {
                channel.sendMessage("https://www.youtube.com/watch?v=gnXUFXc2Yns&ab_channel=ComradeLuigi").queue();
            }
            if ((content.contains("lewd") || content.contains("sex") || content.contains("cum") || content.contains("semen") || content.contains("penis")) && shouldSend) {
                channel.sendMessage("https://tenor.com/view/neptunia-gif-18952040").queue();
            }
            if (content.contains("what if")) {
                channel.sendMessage("https://tryitands.ee/").queue();
            }
        }

    }
}
