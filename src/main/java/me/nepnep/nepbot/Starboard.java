package me.nepnep.nepbot;

import me.nepnep.nepbot.database.StarboardDatabase;
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
            if (!(StarboardDatabase.hasMessage(event.getMessageIdLong())) || event.getChannel().equals(starboard) && event.getGuild().getSelfMember().hasPermission(starboard, Permission.MESSAGE_WRITE)) {
                event.retrieveMessage().queue(message -> {
                    Optional<MessageReaction> optional = message.getReactions()
                            .stream()
                            .filter(reaction -> reaction.getReactionEmote().isEmoji())
                            .filter(reaction -> reaction.getReactionEmote().getEmoji().equals("⭐"))
                            .findFirst();
                    if (optional.isPresent()) {
                        MessageReaction stars = optional.get();
                        stars.retrieveUsers().queue(users -> {
                            User author = message.getAuthor();
                            int starCount = !users.contains(author) ? stars.getCount() : stars.getCount() - 1;
                            if (starCount == 3) {
                                EmbedBuilder builder = new EmbedBuilder();

                                builder.setAuthor(author.getAsTag(), null, author.getAvatarUrl());

                                builder.setDescription(String.format("[Context](%s)%n%s", message.getJumpUrl(), message.getContentRaw()));

                                builder.setTimestamp(message.getTimeCreated());

                                List<Message.Attachment> attachments = message.getAttachments();

                                if (attachments.size() >= 1) {
                                    builder.setImage(attachments.get(0).getUrl());
                                }

                                StarboardDatabase.addMessage(event.getMessageIdLong());

                                starboard.sendMessage(builder.build()).queue();
                            }
                        });
                    }
                });
            }
        }
    }
}
