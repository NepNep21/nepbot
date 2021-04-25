package me.nepnep.nepbot.message.command.commands.mod;

import me.nepnep.nepbot.message.command.ICommand;

import me.nepnep.nepbot.message.command.Category;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class Purge implements ICommand {
    @Override
    public void execute(List<String> args, GuildMessageReceivedEvent event) {
        try {
            String amountString = String.join(" ", args.subList(0, args.size()));
            int amount = Integer.parseInt(amountString);
            event.getChannel().getIterableHistory().takeAsync(amount).thenAccept(event.getChannel()::purgeMessages);
        } catch (Exception e) {
            event.getChannel().sendMessage("Failed!").queue();
        }
    }
    @Override
    public String getInvoke() {
        return "purge";
    }
    @Override
    public Permission requiredPermission() {
        return Permission.MESSAGE_MANAGE;
    }
    @Override
    public Category getCategory() {
        return Category.MOD;
    }
    @Override
    public String getDescription() {
        return "Purges messaages: ;purge <int amount>";
    }
}
