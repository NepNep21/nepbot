package me.nepnep.nepbot.message.command.commands.animals;

import me.nepnep.nepbot.message.command.ICommand;
import me.nepnep.nepbot.Networking;
import me.nepnep.nepbot.message.command.Category;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class Http implements ICommand {
    @Override
    public void execute(List<String> args, GuildMessageReceivedEvent event) {
        if (args.size() >= 1) {
            String cat = "https://http.cat/" + args.get(0);

            if (Networking.pingServer(cat) == 200) {
                event.getChannel().sendMessage(cat).queue();
            } else {
                event.getChannel().sendMessage("https://http.cat/404").queue();
            }
        }
    }
    @Override
    public String getInvoke() {
        return "http";
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
        return "Http cats: ;http <int code>";
    }
}
