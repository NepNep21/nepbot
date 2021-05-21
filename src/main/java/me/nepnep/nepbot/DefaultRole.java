package me.nepnep.nepbot;

import me.nepnep.nepbot.database.DefaultRoleDatabase;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class DefaultRole extends ListenerAdapter {
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        try {
            Guild guild = event.getGuild();
            Role defaultRole = guild.getRolesByName(DefaultRoleDatabase.getDefaultRole(event.getGuild()), false).get(0);

            guild.retrieveMember(event.getUser()).queue(toChange -> event.getGuild().addRoleToMember(toChange, defaultRole).queue());
        } catch (Exception e) {
            // Intentionally empty
        }
    }
}
