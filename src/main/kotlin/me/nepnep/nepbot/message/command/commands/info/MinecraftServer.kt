package me.nepnep.nepbot.message.command.commands.info

import dev.minn.jda.ktx.messages.Embed
import me.nepnep.nepbot.mapper
import me.nepnep.nepbot.message.command.AbstractCommand
import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.request
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class MinecraftServer : AbstractCommand(
    "mcsrv",
    Category.INFO,
    "Gets a minecraft server's info: ;mcsrv <String address>"
) {
    override suspend fun execute(args: List<String>, event: MessageReceivedEvent, channel: GuildMessageChannel) {
        if (args.size != 1) {
            channel.sendMessage("Invalid usage").queue()
            return
        }

        val server = args[0]
        val url = "https://api.mcsrvstat.us/2/$server"
        event.jda.httpClient.request(url, { response ->
            val json = mapper.readTree(response.body!!.string())

            val ip = json["ip"].textValue()
            val port = json["port"].intValue()
            val hostnameJson = json["hostname"]

            if (hostnameJson == null) {
                channel.sendMessage("Couldn't find server").queue()
                return@request
            }

            val hostname = hostnameJson.textValue()

            val jsonMotd = json["motd"]
            if (jsonMotd == null) {
                channel.sendMessage("Couldn't find server").queue()
                return@request
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

            val embed = Embed { 
                field("IP/Hostname", "$ip:$port/$hostname", false)
                field("MOTD", motd, false)
                field("Online players/Max players", "$online/$max", false)
                field("Version", version, false)
                thumbnail = "https://api.mcsrvstat.us/icon/$server"
            }
            channel.sendMessageEmbeds(embed).queue()
        }) {
            channel.sendMessage("API request failed").queue()
        }
    }
}