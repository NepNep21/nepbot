package me.nepnep.nepbot

import okhttp3.*
import java.io.IOException

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