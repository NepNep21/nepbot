package me.nepnep.nepbot.message.command.commands.info;

import me.nepnep.nepbot.message.command.Category;
import me.nepnep.nepbot.message.command.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

public class WhoIs implements ICommand {
    @Override
    public void execute(List<String> args, GuildMessageReceivedEvent event) {
        List<Member> mentioned = event.getMessage().getMentionedMembers();

        if (mentioned.size() >= 1) {
            sendEmbed(mentioned.get(0).getUser(), event);
        } else if (args.size() == 0) {
            sendEmbed(event.getAuthor(), event);
        } else {
            try {
                sendEmbed(event.getJDA().retrieveUserById(Long.parseLong(args.get(0))).complete(), event);
            } catch (NumberFormatException | NullPointerException | ErrorResponseException e) {
                event.getChannel().sendMessage("Invalid id!").queue();
            }
        }
    }
    private void sendEmbed(User user, GuildMessageReceivedEvent event) {
        EmbedBuilder builder = new EmbedBuilder();
        LocalDate now = LocalDate.now();
        TextChannel channel = event.getChannel();
        String id = user.getId();
        String tag = user.getAsTag();
        String avatarUrl = user.getAvatarUrl();

        OffsetDateTime timeCreated = user.getTimeCreated();
        LocalDate createdDate = timeCreated.toLocalDate();
        long sinceCreated = ChronoUnit.DAYS.between(createdDate, now);
        try {
            Member member = event.getGuild().retrieveMember(user).complete();

            OffsetDateTime timeJoined = member.getTimeJoined();
            LocalDate joinedDate = timeJoined.toLocalDate();
            long sinceJoined = ChronoUnit.DAYS.between(joinedDate, now);

            List<Role> roles = member.getRoles();
            List<String> rolesString = roles.stream()
                    .map(Role::getName)
                    .collect(Collectors.toList());
            EnumSet<Permission> permissions = member.getPermissions();
            List<String> permissionsString = permissions.stream()
                    .map(Permission::getName)
                    .collect(Collectors.toList());

            builder.setTitle(String.format("%s/%s", id, tag));

            builder.setThumbnail(avatarUrl);

            builder.addField("Created on: ", String.format("%s-%s-%s", timeCreated.getDayOfMonth(), timeCreated.getMonthValue(), timeCreated.getYear()), false);

            builder.addField("Days since created: ", String.valueOf(sinceCreated), false);

            builder.addField("Joined on: ", String.format("%s-%s-%s", timeJoined.getDayOfMonth(), timeJoined.getMonthValue(), timeJoined.getYear()), false);

            builder.addField("Days since joined: ", String.valueOf(sinceJoined), false);

            builder.addField("Roles: ", String.valueOf(rolesString), false);

            builder.addField("Permissions: ", String.valueOf(permissionsString), false);

            channel.sendMessage(builder.build()).queue();
        } catch (ErrorResponseException e) {
            builder.setTitle(String.format("%s/%s", id, tag));

            builder.setThumbnail(avatarUrl);

            builder.addField("Created on: ", String.format("%s-%s-%s", timeCreated.getDayOfMonth(), timeCreated.getMonthValue(), timeCreated.getYear()), false);

            builder.addField("Days since created: ", String.valueOf(sinceCreated), false);

            builder.addField("Not in guild", ":x:", false);

            channel.sendMessage(builder.build()).queue();
        }
    }
    @Override
    public String getInvoke() {
        return "whois";
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
        return "Gets a user's information: ;whois <Mention member> | <long id> | null";
    }
}
