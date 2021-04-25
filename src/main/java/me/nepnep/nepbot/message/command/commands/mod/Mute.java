package me.nepnep.nepbot.message.command.commands.mod;

import me.nepnep.nepbot.message.command.ICommand;
import me.nepnep.nepbot.message.command.Category;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;

import java.util.List;

public class Mute implements ICommand {
    @Override
    public void execute(List<String> args, GuildMessageReceivedEvent event) {
        List<Member> mentioned = event.getMessage().getMentionedMembers();
        Guild guild = event.getGuild();
        Member self = guild.getSelfMember();
        TextChannel channel = event.getChannel();

        if (mentioned.isEmpty() || args.size() < 2) {
            channel.sendMessage(":x: Incorrect usage").queue();
            return;
        }
        if(mentioned.size() > 1) {
            channel.sendMessage(":x: Please only mention one user.").queue();
            return;
        }
        Member toMute = mentioned.get(0);
        String reason = String.join(" ", args.subList(1, args.size()));
        Member author = guild.retrieveMember(event.getAuthor()).complete();

        if (!author.canInteract(toMute)) {
            channel.sendMessage(":x: You don't have permission to use this command.").queue();
            return;
        }
        if (!self.hasPermission(Permission.MANAGE_ROLES) || !self.canInteract(toMute)) {
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
            guild.addRoleToMember(toMute, muted).queue();
            channel.sendMessage(String.format("%s muted %s, for reason: `%s`",event.getAuthor(),toMute,reason)).queue();
        } catch (HierarchyException e) {
            channel.sendMessage(":x: I can't manage the Muted role, it is higher than my max role!").queue();
        }
    }
    @Override
    public String getInvoke() {
        return "mute";
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
        return "Mutes someone: ;mute <Mention member> <String reason>";
    }
}
