package me.nepnep.nepbot.message.command.commands.info

import com.fasterxml.jackson.databind.ObjectMapper
import me.nepnep.nepbot.message.command.Category
import me.nepnep.nepbot.message.command.ICommand
import me.nepnep.nepbot.request
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

class MinecraftServer : ICommand {
    override fun execute(args: List<String>, event: GuildMessageReceivedEvent) {
        val channel = event.channel
        if (args.size != 1) {
            channel.sendMessage("Invalid usage").queue()
            return
        }

        val server = args[0]
        val url = "https://api.mcsrvstat.us/2/$server"
        event.jda.httpClient.request(url, { response ->
            val json = ObjectMapper().readTree(response.body()!!.string())

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

            val embed = EmbedBuilder()
                .addField("IP/Hostname", "$ip:$port/$hostname", false)
                .addField("MOTD", motd, false)
                .addField("Online players/Max players", "$online/$max", false)
                .addField("Version", version, false)
                .setThumbnail("https://api.mcsrvstat.us/icon/$server")
                .build()
            channel.sendMessageEmbeds(embed).queue()
        }) {
            channel.sendMessage("API request failed").queue()
        }
    }

    override fun getInvoke() = "mcsrv"

    override fun getCategory() = Category.INFO

    override fun getDescription() = "Gets a minecraft server's info: ;mcsrv <String address>"
}