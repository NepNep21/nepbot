package me.nepnep.nepbot.plugin

import dev.minn.jda.ktx.events.CoroutineEventListener
import dev.minn.jda.ktx.events.listener
import me.nepnep.nepbot.jda
import me.nepnep.nepbot.message.command.AbstractCommand
import me.nepnep.nepbot.message.command.CommandRegister
import net.dv8tion.jda.api.events.GenericEvent
import java.net.URLClassLoader

open class Plugin {
    val loader = javaClass.classLoader as URLClassLoader
    lateinit var info: PluginInfo
    lateinit var path: String
    
    protected val commands = mutableListOf<AbstractCommand>()
    protected val listeners = mutableListOf<CoroutineEventListener>()
    
    open fun onLoad() {
        commands.forEach(CommandRegister::register)
    }
    
    open fun onUnload() {
        listeners.forEach(jda::removeEventListener)
        commands.forEach(CommandRegister::remove)
    }

    protected inline fun <reified T : GenericEvent> addListener(crossinline it: suspend CoroutineEventListener.(T) -> Unit) {
        listeners.add(jda.listener(consumer = it))
    }
}