package me.nepnep.nepbot.message.command.commands.info

import com.fasterxml.jackson.databind.ObjectMapper
import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.message.command.ICommand
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class MinecraftServer : ICommand {
    override fun execute(args: List<String>, event: GuildMessageReceivedEvent) {
        val channel = event.channel
        if (args.size != 1) {
            channel.sendMessage("Invalid usage").queue()
            return
        }

        val server = args[0]
        val url = "https://api.mcsrvstat.us/2/$server"
        val request = Request.Builder().url(url).build()
        event.jda.httpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                channel.sendMessage("API request failed").queue()
            }

            override fun onResponse(call: Call, response: Response) {
                val json = ObjectMapper().readTree(response.body()!!.string())

                val ip = json["ip"].textValue()
                val port = json["port"].intValue()
                val hostnameJson = json["hostname"]

                if (hostnameJson == null) {
                    channel.sendMessage("Couldn't find server").queue()
                    return
                }

                val hostname = hostnameJson.textValue()

                val jsonMotd = json["motd"]
                if (jsonMotd == null) {
                    channel.sendMessage("Couldn't find server").queue()
                    return
                }

                val builder = StringBuilder()
                jsonMotd["clean"].elements().forEach {
                    builder.append(it.textValue())
                }
                val motd = builder.toString()

                val players = json["players"]
                val online = players["online"].intValue()
                val max = players["max"].intValue()

                val version = json["version"].textValue()

                val embed = EmbedBuilder()
                    .addField("IP/Hostname", "$ip:$port/$hostname", false)
                    .addField("MOTD", motd, false)
                    .addField("Online players/Max players", "$online/$max", false)
                    .addField("Version", version, false)
                    .setThumbnail("https://api.mcsrvstat.us/icon/$server")
                    .build()
                channel.sendMessageEmbeds(embed).queue()
            }
        })
    }

    override fun getInvoke() = "mcsrv"

    override fun getCategory() = Category.INFO

    override fun getDescription() = "Gets a minecraft server's info: ;mcsrv <String address>"
}