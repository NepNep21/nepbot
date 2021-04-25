package me.nepnep.nepbot.message.command;

import me.nepnep.nepbot.message.command.commands.*;
import me.nepnep.nepbot.message.command.commands.admin.*;
import me.nepnep.nepbot.message.command.commands.animals.*;
import me.nepnep.nepbot.message.command.commands.fun.*;
import me.nepnep.nepbot.message.command.commands.info.*;
import me.nepnep.nepbot.message.command.commands.mod.Ban;
import me.nepnep.nepbot.message.command.commands.mod.Kick;
import me.nepnep.nepbot.message.command.commands.mod.Mute;
import me.nepnep.nepbot.message.command.commands.mod.Purge;
import me.nepnep.nepbot.message.command.commands.mod.Unmute;

import java.util.HashMap;
import java.util.Map;

public class CommandRegister {
    public static final Map<String, ICommand> REGISTER = new HashMap<>();

    public static void registerCommands() {
        register(new Ping());
        register(new Help());
        register(new TinyRat());
        register(new BigRat());
        register(new Stat());
        register(new Kick());
        register(new Ban());
        register(new Mute());
        register(new Purge());
        register(new SetDefaultRole());
        register(new GiveDefaultRole());
        register(new Unmute());
        register(new Steal());
        register(new BigSnail());
        register(new TinySnail());
        register(new Http());
        register(new Xkcd());
        register(new BigDog());
        register(new XmasRat());
        register(new BigMonkey());
        register(new WhoIs());
        register(new Lmddgtfy());
        register(new BigEars());
        register(new LewdBlackListCommand());
        register(new RemoveLewdBlackList());
        register(new RandomChimpEvent());
        register(new Suggestion());
        register(new Shut());
        register(new ProfilePicture());
        register(new MinecraftServer());
        register(new BotInfo());
        register(new UrbanDictionary());
        register(new BottomCommand());
        register(new BottomWhiteListCommand());
        register(new RemoveBottomWhiteList());
        register(new JoinMessage());
        register(new Shutdown());
    }
    private static void register(ICommand command) {
        if (!REGISTER.containsKey(command.getInvoke())) {
            REGISTER.put(command.getInvoke().toLowerCase(), command);
        }
    }
}
