package me.nepnep.nepbot.message.command

import me.nepnep.nepbot.message.command.commands.*
import me.nepnep.nepbot.message.command.commands.`fun`.BottomCommand
import me.nepnep.nepbot.message.command.commands.`fun`.Lmddgtfy
import me.nepnep.nepbot.message.command.commands.`fun`.Xkcd
import me.nepnep.nepbot.message.command.commands.admin.*
import me.nepnep.nepbot.message.command.commands.animals.*
import me.nepnep.nepbot.message.command.commands.info.*
import me.nepnep.nepbot.message.command.commands.mod.*

object CommandRegister {
    val register = mutableMapOf<String, ICommand>()

    fun registerCommands() {
        register(BottomWhitelist())
        register(GiveDefaultRole())
        register(JoinMessage())
        register(LewdBlacklist())
        register(RemoveBottomWhitelist())
        register(RemoveLewdBlacklist())
        register(SetDefaultRole())
        register(Steal())
        register(BigDog())
        register(BigMonkey())
        register(BigRat())
        register(BigSnail())
        register(HttpCat())
        register(RandomChimpEvent())
        register(Shut())
        register(TinyCat())
        register(TinyRat())
        register(XmasRat())
        register(BottomCommand())
        register(Lmddgtfy())
        register(Xkcd())
        register(BotInfo())
        register(MinecraftServer())
        register(ProfilePicture())
        register(UrbanDictionary())
        register(WhoIs())
        register(Ban())
        register(Kick())
        register(Mute())
        register(Purge())
        register(Unmute())
        register(Help())
        register(Ping())
        register(Shutdown())
        register(Suggestion())
        register(Poll())
    }

    private fun register(command: ICommand) {
        register[command.getInvoke()] = command
    }
}