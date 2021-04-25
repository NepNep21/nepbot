package me.nepnep.nepbot.message.command.commands.mod;

import me.nepnep.nepbot.message.command.ICommand;
import me.nepnep.nepbot.message.command.Category;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.ContextException;

import java.util.List;

public class Kick implements ICommand {
    @Override
    public void execute(List<String> args, GuildMessageReceivedEvent event) {
        List<Member> mentioned = event.getMessage().getMentionedMembers();
        TextChannel channel = event.getChannel();
        if (mentioned.isEmpty() || args.size() < 2 || mentioned.size() > 1) {
            channel.sendMessage("Failed, make sure to mention one member and specify a reason").queue();
            return;
        }
        Member toKick = mentioned.get(0);
        String reason = String.join(" ", args.subList(1, args.size()));
        Guild guild = event.getGuild();
        User author = event.getAuthor();

        guild.retrieveMember(author).queue(member -> {
            if (member.canInteract(toKick) && guild.getSelfMember().canInteract(toKick) && guild.getSelfMember().hasPermission(Permission.KICK_MEMBERS)) {
                channel.sendMessage(String.format("%s kicked %s for reason: %s", author, toKick, reason)).queue();
                // Try-catch may or may not be required
                try {
                    toKick.getUser().openPrivateChannel().queue(privateChannel -> {
                        privateChannel.sendMessage(String.format("You have been kicked from %s, by %s, reason: %s", guild, author.getAsTag(), reason)).queue();
                    });
                    throw new ContextException();
                } catch (ContextException e) {
                    // Intentionally empty
                }

                guild.kick(toKick, reason).queue();
            }
        });

    }

    @Override
    public String getInvoke() {
        return "kick";
    }

    @Override
    public Permission requiredPermission() {
        return Permission.KICK_MEMBERS;
    }

    @Override
    public Category getCategory() {
        return Category.MOD;
    }

    @Override
    public String getDescription() {
        return "Kicks someone: ;kick <Mention member> <String reason>";
    }
}
