package me.nepnep.nepbot.plugin

import me.nepnep.nepbot.mapper
import java.io.File
import java.net.URLClassLoader

object PluginManager {
    private val plugins = mutableMapOf<String, Plugin>()
    
    fun load(file: File) {
        val loader = URLClassLoader(arrayOf(file.toURI().toURL()))
        val info = mapper.readValue(loader.getResourceAsStream("plugin.json"), PluginInfo::class.java)

        if (plugins.contains(info.name)) {
            loader.close()
            return
        }

        plugins[info.name] = (loader.loadClass(info.main).newInstance() as Plugin).also {
            it.info = info
            it.path = file.canonicalPath
            it.onLoad() 
        }
    }
    
    fun loadAll() {
        File("plugins").listFiles()?.forEach {
            load(it)
        }
    }
    
    fun unload(plugin: String): String {
        val path = plugins.getValue(plugin).let { 
            it.onUnload()
            it.loader.close()
            
            it.path
        }
        plugins.remove(plugin)
        
        return path
    }

    fun contains(it: String) = plugins.containsKey(it)
    
    fun reload() {
        loadAll()
        plugins.keys.map(this::unload).forEach { load(File(it)) }
    }

    fun getInfo(name: String) = plugins[name]?.info
}