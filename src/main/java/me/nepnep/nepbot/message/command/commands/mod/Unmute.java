package me.nepnep.nepbot.message.command.commands.mod;

import me.nepnep.nepbot.message.command.ICommand;
import me.nepnep.nepbot.message.command.Category;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class Unmute implements ICommand {
    @Override
    public void execute(List<String> args, GuildMessageReceivedEvent event) {
        List<Member> mentioned = event.getMessage().getMentionedMembers();
        Guild guild = event.getGuild();
        Member self = guild.getSelfMember();
        TextChannel channel = event.getChannel();

        if (mentioned.isEmpty()) {
            channel.sendMessage(":x: Incorrect usage").queue();
            return;
        }
        if (mentioned.size() > 1) {
            channel.sendMessage(":x: Please only mention one user.").queue();
            return;
        }
        Member toUnmute = mentioned.get(0);
        Member author = guild.retrieveMember(event.getAuthor()).complete();

        if (!author.canInteract(toUnmute)) {
            channel.sendMessage(":x: You don't have permission to use this command.").queue();
            return;
        }
        if (!self.hasPermission(Permission.MANAGE_ROLES) || !self.canInteract(toUnmute)) {
            channel.sendMessage(":x: I can't manage roles that user or I don't have permission to manage roles.").queue();
            return;
        }
        List<Role> roles = guild.getRolesByName("Muted", true);

        Role muted;

        try {
            muted = roles.get(0);
        } catch (Exception e) {
            channel.sendMessage(":x: Unable to find role named \"Muted\" (Not case sensitive)").queue();
            return;
        }
        try {
            guild.removeRoleFromMember(toUnmute, muted).queue();
            channel.sendMessage(String.format("Unmuted %s", toUnmute)).queue();
        } catch (Exception e) {
            channel.sendMessage("Failed!").queue();
        }
    }

    @Override
    public String getInvoke() {
        return "unmute";
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
        return "Unmutes someone: ;unmute <Mention member>";
    }
}
