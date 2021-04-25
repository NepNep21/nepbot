package me.nepnep.nepbot.message.command.commands.info;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.nepnep.nepbot.message.command.Category;
import me.nepnep.nepbot.message.command.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import okhttp3.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class MinecraftServer implements ICommand {
    @Override
    public void execute(List<String> args, GuildMessageReceivedEvent event) {
        if (args.size() >= 1) {
            TextChannel channel = event.getChannel();
            String address = args.get(0);
            String url = "https://api.mcsrvstat.us/2/" + address;

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            Call call = client.newCall(request);

            try {
                Response response = call.execute();
                ResponseBody body = response.body();

                ObjectMapper mapper = new ObjectMapper();
                EmbedBuilder builder = new EmbedBuilder();
                JSONObject server = mapper.readValue(body.string(), JSONObject.class);

                String cleanMotd = server.getMotd().get("clean").toString().replace(",", "").replace("nig", "no");

                builder.addField("IP/Hostname", String.format("%s:%d/%s", server.getIp(), server.getPort(), server.getHostname()), false);

                builder.addField("MOTD", cleanMotd, false);

                builder.addField("Online players/Max players", String.format("%s/%s", server.getPlayers().get("online"), server.getPlayers().get("max")), false);

                builder.addField("Version", server.getVersion(), false);

                builder.setThumbnail("https://api.mcsrvstat.us/icon/" + address);

                channel.sendMessage(builder.build()).queue();
                response.close();
            } catch (IOException | NullPointerException e) {
                channel.sendMessage("Invalid, offline, or unreachable server").queue();
            }
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class JSONObject {
        String ip;

        String hostname;

        int port;

        Map<String, List<String>> motd;

        Map<String, Object> players;

        String version;

        public String getIp() {
            return ip;
        }

        public String getHostname() {
            return hostname;
        }

        public int getPort() {
            return port;
        }

        public Map<String, List<String>> getMotd() {
            return motd;
        }

        public Map<String, Object> getPlayers() {
            return players;
        }

        public String getVersion() {
            return version;
        }
    }


    @Override
    public String getInvoke() {
        return "mcsrv";
    }

    @Override
    public Permission requiredPermission() {
        return null;
    }

    @Override
    public Category getCategory() {
        return Category.INFO;
    }

    @Override
    public String getDescription() {
        return "Gets a minecraft server's info: ;mcsrv <String address>";
    }
}
