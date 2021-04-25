package me.nepnep.nepbot.message.command.commands.admin;

import me.nepnep.nepbot.Database;
import me.nepnep.nepbot.message.command.ICommand;
import me.nepnep.nepbot.message.command.Category;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class SetDefaultRole implements ICommand {
    @Override
    public void execute(List<String> args, GuildMessageReceivedEvent event) {
        if (event.getMessage().getMentionedRoles().size() != 0) {
            Role role = event.getMessage().getMentionedRoles().get(0);

            Database.write(event.getGuild(), role.getName());
        } else {
            event.getChannel().sendMessage("Please mention a role").queue();
        }
    }
    @Override
    public String getInvoke() {
        return "setdefaultrole";
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
        return "Sets the default role: ;setdefaultrole <Mention role>";
    }
}
