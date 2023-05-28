package me.nepnep.nepbot.message.command

import me.nepnep.nepbot.message.command.commands.*
import me.nepnep.nepbot.message.command.commands.`fun`.BottomCommand
import me.nepnep.nepbot.message.command.commands.`fun`.Lmddgtfy
import me.nepnep.nepbot.message.command.commands.`fun`.Xkcd
import me.nepnep.nepbot.message.command.commands.admin.*
import me.nepnep.nepbot.message.command.commands.animals.*
import me.nepnep.nepbot.message.command.commands.info.*
import me.nepnep.nepbot.message.command.commands.mod.*
import me.nepnep.nepbot.message.command.commands.music.*

object CommandRegister {
    val register = mutableMapOf<String, AbstractCommand>()

    fun registerCommands() {
        register(BottomWhitelist())
        register(GiveDefaultRole())
        register(JoinMessage())
        register(RemoveBottomWhitelist())
        register(SetDefaultRole())
        register(Steal())
        register(BigRat())
        register(HttpCat())
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
        register(Prefix())
        register(Play())
        register(Skip())
        register(Stop())
        register(Search())
        register(Volume())
        register(StealSticker())
        register(Reload())
        register(Load())
        register(Unload())
        register(Info())
        register(SwitchPlayer())
    }

    fun register(command: AbstractCommand) {
        register[command.invoke] = command
    }
    
    fun remove(command: AbstractCommand) {
        register.remove(command.invoke)
    }
}
