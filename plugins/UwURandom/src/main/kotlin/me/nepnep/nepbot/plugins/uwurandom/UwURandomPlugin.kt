package me.nepnep.nepbot.plugins.uwurandom

import me.nepnep.nepbot.plugin.Plugin

class UwURandomPlugin : Plugin() {
    override fun onLoad() {
        commands.add(UwURandom())
        super.onLoad()
    }
}