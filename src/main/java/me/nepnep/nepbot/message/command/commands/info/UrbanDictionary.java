package me.nepnep.nepbot.message.command.commands.info;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.nepnep.nepbot.Main;
import me.nepnep.nepbot.message.command.Category;
import me.nepnep.nepbot.message.command.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import okhttp3.Request;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

public class UrbanDictionary implements ICommand {

    @Override
    public void execute(List<String> args, GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        if (args.size() >= 1) {
            String api = "http://api.urbandictionary.com/v0/define?term=";
            String query = String.join(" ", args.subList(0, args.size()));
            ResponseBody body = null;
            try {
                String safeQuery = URLEncoder.encode(query, "UTF-8");
                Request request = new Request.Builder().url(api + safeQuery).build();
                body = Main.HTTP_CLIENT.newCall(request).execute().body();
                ObjectMapper mapper = new ObjectMapper();

                JsonNode json = mapper.readTree(body.string());
                JsonNode list = json.get("list");

                if (list == null) {
                    body.close();
                    channel.sendMessage("Not found or unknown error").queue();
                    return;
                }

                JsonNode best = list.get(0);

                if (best == null) {
                    body.close();
                    channel.sendMessage("Not found").queue();
                    return;
                }

                String definition = best.get("definition").asText();
                String example = best.get("example").asText();

                String safeDefinition = definition.replaceAll("nigg|fag", "[Removed]");
                String safeExample = example.replaceAll("nigg|fag", "[Removed]");

                EmbedBuilder builder = new EmbedBuilder();
                builder.addField("Definition:", safeDefinition, false);
                builder.addField("Example:", safeExample, false);

                channel.sendMessage(builder.build()).queue();
            } catch (IOException e) {
                channel.sendMessage("API Issues, try again later.").queue();
            } catch (IllegalArgumentException e) {
                channel.sendMessage("Embed is too large!").queue();
            } finally {
                if (body != null) {
                    body.close();
                }
            }
        } else {
            channel.sendMessage("Invalid usage!").queue();
        }
    }

    @Override
    public String getInvoke() {
        return "urban";
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
        return "Get a thing's definition from urban dictionary! ;urban <query>";
    }
}
