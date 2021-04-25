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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

public class UrbanDictionary implements ICommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(UrbanDictionary.class);

    @Override
    public void execute(List<String> args, GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        if (args.size() >= 1) {
            String api = "http://urbanscraper.herokuapp.com/";
            String query = String.join(" ", args.subList(0, args.size()));
            ResponseBody body = null;
            try {
                String safeQuery = "define/" + URLEncoder.encode(query, "UTF-8");
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(api + safeQuery).build();
                Response response = client.newCall(request).execute();
                body = response.body();
                ObjectMapper mapper = new ObjectMapper();

                if (response.code() == 200) {
                    JSONObject json = mapper.readValue(body.string(), JSONObject.class);

                    String definition = json.getDefinition();
                    String safeDefinition = definition.replace("nig", "no");
                    String example = json.getExample();
                    String safeExample = example.replace("nig", "no");

                    EmbedBuilder builder = new EmbedBuilder();

                    builder.addField("Definition:", safeDefinition, false);
                    builder.addField("Example:", safeExample, false);

                    channel.sendMessage(builder.build()).queue();
                } else {
                    channel.sendMessage("Nothing found!").queue();
                }

            } catch (IOException e) {
                LOGGER.error("An exception occurred at UrbanDictionary!", e);
            } finally {
                if (body != null) {
                    body.close();
                }
            }
        } else {
            channel.sendMessage("Invalid usage!").queue();
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class JSONObject {
        String definition;
        String example;

        public String getExample() {
            return example;
        }

        public String getDefinition() {
            return definition;
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
