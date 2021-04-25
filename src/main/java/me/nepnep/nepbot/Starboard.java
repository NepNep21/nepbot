package me.nepnep.nepbot;

import me.nepnep.nepbot.json.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;
import java.util.Optional;

public class Starboard extends ListenerAdapter {
    @Override
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
        List<TextChannel> starboards = event.getGuild().getTextChannelsByName("starboard", false);
        if (starboards.size() >= 1) {
            TextChannel starboard = starboards.get(0);
            List<Long> messages = Util.readListFromJsonArray("starboard.json");
            if (!(messages.contains(event.getMessageIdLong()) || event.getChannel().equals(starboard)) && event.getGuild().getSelfMember().hasPermission(starboard, Permission.MESSAGE_WRITE)) {
                Optional<MessageReaction> optional = event.retrieveMessage()
                        .map(Message::getReactions)
                        .complete()
                        .stream()
                        .filter(reaction -> reaction.getReactionEmote().isEmoji())
                        .filter(reaction -> reaction.getReactionEmote().getEmoji().equals("⭐"))
                        .findFirst();
                if (optional.isPresent()) {
                    MessageReaction stars = optional.get();
                    User author = event.retrieveMessage().map(Message::getAuthor).complete();
                    int starCount = !stars.retrieveUsers().complete().contains(author) ? stars.getCount() : stars.getCount() - 1;

                    if (starCount == 3) {
                        EmbedBuilder builder = new EmbedBuilder();
                        Message message = event.retrieveMessage().complete();

                        builder.setAuthor(author.getAsTag(), null, author.getAvatarUrl());

                        builder.setDescription(String.format("[Context](%s)%n%s", message.getJumpUrl(), message.getContentRaw()));

                        builder.setTimestamp(message.getTimeCreated());

                        List<Message.Attachment> attachments = message.getAttachments();

                        if (attachments.size() >= 1) {
                            builder.setImage(attachments.get(0).getUrl());
                        }

                        messages.add(message.getIdLong());
                        Util.writeListToJsonArray(messages, "starboard.json");

                        starboard.sendMessage(builder.build()).queue();
                    }
                }
            }
        }
    }
}
