package me.nepnep.nepbot.message.command.commands.fun;

import com.github.bottomSoftwareFoundation.bottom.Bottom;
import com.github.bottomSoftwareFoundation.bottom.TranslationError;
import me.nepnep.nepbot.json.BottomWhiteList;
import me.nepnep.nepbot.message.command.Category;
import me.nepnep.nepbot.message.command.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class BottomCommand implements ICommand {
    @Override
    public void execute(List<String> args, GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        if (BottomWhiteList.getList().contains(event.getChannel().getIdLong()) && args.size() >= 2) {
            String string = String.join(" ", args.subList(1, args.size()));

            try {
                if (args.get(0).equals("encode")) {
                    channel.sendMessage(Bottom.encode(string)).queue();
                } else if (args.get(0).equals("decode")) {
                    channel.sendMessage(Bottom.decode(string)).queue();
                }
            } catch (IllegalArgumentException e) {
                channel.sendMessage("Resulting message must be <2000 characters").queue();
            } catch (TranslationError e) {
                channel.sendMessage("Invalid bottom string").queue();
            }
        } else {
            channel.sendMessage("Invalid usage or the channel isn't whitelisted!").queue();
        }
    }

    @Override
    public String getInvoke() {
        return "bottom";
    }

    @Override
    public Permission requiredPermission() {
        return null;
    }

    @Override
    public Category getCategory() {
        return Category.FUN;
    }

    @Override
    public String getDescription() {
        return "Encodes/Decodes to bottom: ;bottom <encode> | <decode> <String string>";
    }
}
