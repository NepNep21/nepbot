package me.nepnep.nepbot.message.command.commands.admin;

import me.nepnep.nepbot.Database;
import me.nepnep.nepbot.message.command.ICommand;
import me.nepnep.nepbot.message.command.Category;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

public class GiveDefaultRole implements ICommand {
    @Override
    public void execute(List<String> args, GuildMessageReceivedEvent event) {
        event.getGuild().loadMembers().onSuccess(members -> {


        try {
            List<Member> noRole = new ArrayList<>();
            Role role = event.getGuild().getRolesByName(Database.read(event.getGuild()), false).get(0);

            for (Member member : members) {
                if (member.getRoles().isEmpty()) {
                    noRole.add(member);
                }
            }
            for (Member member : noRole) {
                event.getGuild().addRoleToMember(member, role).queue();
            }
        } catch (Exception e) {
            event.getChannel().sendMessage("Failed!").queue();
        }
      });
    }
    @Override
    public String getInvoke() {
        return "givedefaultrole";
    }
    @Override
    public Permission requiredPermission() {
        return Permission.MANAGE_ROLES;
    }
    @Override
    public Category getCategory() {
        return Category.ADMIN;
    }
    @Override
    public String getDescription() {
        return "Gives the default role to all members without one";
    }
}
