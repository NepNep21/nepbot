package me.nepnep.nepbot.message.command.commands;

import me.nepnep.nepbot.message.command.Category;
import me.nepnep.nepbot.message.command.CommandRegister;
import me.nepnep.nepbot.message.command.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class Help implements ICommand {
    @Override
    public void execute(List<String> args, GuildMessageReceivedEvent event) {
        EmbedBuilder builder = new EmbedBuilder();
        String categories = Arrays.toString(Arrays.stream(Category.values()).map(Enum::toString).toArray(String[]::new));
        TextChannel channel = event.getChannel();

        if (args.size() == 0) {
            String categoriesFormatted = String.format(categories
                    .replace("[", "")
                    .replace("]", "")
                    .replace(", ", "%n"));

            String general = getDescriptions(Category.GENERAL);

            builder.setDescription(String.format("General:%nCategories:%n%n%s%n%nCommands:%n%n%s", categoriesFormatted, general));

            channel.sendMessage(builder.build()).queue();
        } else {
            try {
                int page = Integer.parseInt(args.get(0));
                switch (page) {
                    case 1:
                        builder.setDescription(getDescriptions(Category.ADMIN));
                        channel.sendMessage(builder.build()).queue();
                        break;
                    case 2:
                        builder.setDescription(getDescriptions(Category.ANIMALS));
                        channel.sendMessage(builder.build()).queue();
                        break;
                    case 3:
                        builder.setDescription(String.format("JoinMessage%n%nDefaultRole%n%nLeaveMessage%n%nStarboard"));
                        channel.sendMessage(builder.build()).queue();
                        break;
                    case 4:
                        builder.setDescription(getDescriptions(Category.FUN));
                        channel.sendMessage(builder.build()).queue();
                        break;
                    case 5:
                        builder.setDescription(getDescriptions(Category.INFO));
                        channel.sendMessage(builder.build()).queue();
                        break;
                    case 6:
                        builder.setDescription(String.format("Communism%n%nOh my how lewd%n%nWhat if?"));
                        channel.sendMessage(builder.build()).queue();
                        break;
                    case 7:
                        builder.setDescription(getDescriptions(Category.MOD));
                        channel.sendMessage(builder.build()).queue();
                        break;
                    default:
                        channel.sendMessage("Invalid page!").queue();
                }
            } catch (NumberFormatException e) {
                channel.sendMessage("Invalid page!").queue();
            }
        }
    }
    private String getDescriptions(Category category) {
        StringBuilder builder = new StringBuilder();
        ICommand[] commands = CommandRegister.REGISTER
                .values()
                .stream()
                .filter(command -> command.getCategory().equals(category))
                .toArray(ICommand[]::new);

        for (ICommand command : commands) {
            builder.append(command.getInvoke()).append(String.format("%n%s%n%n", command.getDescription()));
        }
        return builder.toString();
    }
    @Override
    public String getInvoke() {
        return "help";
    }
    @Override
    public Permission requiredPermission() {
        return null;
    }
    @Override
    public Category getCategory() {
        return Category.GENERAL;
    }
    @Override
    public String getDescription() {
        return "Help page: ;help <int page> | null";
    }
}