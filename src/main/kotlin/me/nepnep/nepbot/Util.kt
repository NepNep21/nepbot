package me.nepnep.nepbot

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel
import okhttp3.*
import java.io.IOException
import java.net.URL

internal val QUOTED_REGEX = "\".+?\"".toRegex()

suspend inline fun OkHttpClient.request(
    url: String,
    crossinline success: (Response) -> Unit,
    crossinline failure: (IOException) -> Unit
) {
    withContext(Dispatchers.IO) {
        newCall(Request.Builder().url(url).build()).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                failure(e)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use(success)
            }
        })
    }
}

fun URL.isDiscord(): Boolean {
    val domainLevels = host.split('.')
    return try {
        val registeredDomain = domainLevels.subList(domainLevels.size - 2, domainLevels.size).joinToString(".")
        registeredDomain in arrayOf("discordapp.net", "discordapp.com")
    } catch (e: IndexOutOfBoundsException) {
        false
    }
}

fun Member.canSend(channel: GuildMessageChannel): Boolean {
    return if (channel is ThreadChannel) {
        hasPermission(channel, Permission.MESSAGE_SEND_IN_THREADS)
    } else {
        hasPermission(channel, Permission.MESSAGE_SEND)
    }
}