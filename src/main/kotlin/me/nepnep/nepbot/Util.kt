package me.nepnep.nepbot

import okhttp3.*
import java.io.IOException
import java.net.URL

inline fun OkHttpClient.request(
    url: String,
    crossinline success: (Response) -> Unit,
    crossinline failure: (IOException) -> Unit
) {
    newCall(Request.Builder().url(url).build()).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            failure(e)
        }

        override fun onResponse(call: Call, response: Response) {
            response.use(success)
        }
    })
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