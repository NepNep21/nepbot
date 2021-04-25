package me.nepnep.nepbot.message.command.commands.animals;

import me.nepnep.nepbot.message.command.ICommand;
import me.nepnep.nepbot.message.command.Category;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class BigEars implements ICommand {
    @Override
    public void execute(List<String> args, GuildMessageReceivedEvent event) {
        event.getChannel().sendMessage("https://bigears.monster/").queue();
    }
    @Override
    public String getInvoke() {
        return "bigears";
    }
    @Override
    public Permission requiredPermission() {
        return null;
    }
    @Override
    public Category getCategory() {
        return Category.ANIMALS;
    }
    @Override
    public String getDescription() {
        return "Big ears";
    }
}
