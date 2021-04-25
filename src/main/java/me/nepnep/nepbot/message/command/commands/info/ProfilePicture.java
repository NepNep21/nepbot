package me.nepnep.nepbot.message.command.commands.info;

import me.nepnep.nepbot.message.command.Category;
import me.nepnep.nepbot.message.command.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;

import java.util.List;

public class ProfilePicture implements ICommand {
    @Override
    public void execute(List<String> args, GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        String authorUrl = event.getAuthor().getAvatarUrl();
        List<Member> mentioned = event.getMessage().getMentionedMembers();

        if (args.size() == 0 && authorUrl != null) {
            channel.sendMessage(authorUrl + "?size=2048").queue();
            return;
        }
        if (mentioned.size() >= 1) {
            String mentionedUrl = mentioned.get(0).getUser().getAvatarUrl();
            if (mentionedUrl != null) {
                channel.sendMessage(mentionedUrl + "?size=2048").queue();
            }
            return;
        }
        if (args.size() >= 1) {
            try {
                User user = event.getJDA().retrieveUserById(args.get(0)).complete();
                String url = user.getAvatarUrl();

                if (url != null) {
                    channel.sendMessage(url + "?size=2048").queue();
                }
            } catch (NumberFormatException | ErrorResponseException e) {
                channel.sendMessage("Invalid id!").queue();
            }
        }
    }
    @Override
    public String getInvoke() {
        return "pfp";
    }
    @Override
    public Permission requiredPermission() {
        return null;
    }
    @Override
    public Category getCategory() {
        return Category.INFO;
    }
    @Override
    public String getDescription() {
        return "Sends the avatar url of a user: ;pfp <Mention member> | <long id> | null";
    }
}
