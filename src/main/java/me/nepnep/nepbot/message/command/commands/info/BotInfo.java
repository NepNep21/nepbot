package me.nepnep.nepbot.message.command.commands.info;

import com.sun.management.OperatingSystemMXBean;
import me.nepnep.nepbot.message.command.Category;
import me.nepnep.nepbot.message.command.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDAInfo;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.lang.management.ManagementFactory;
import java.util.List;

public class BotInfo implements ICommand {
    @Override
    public void execute(List<String> args, GuildMessageReceivedEvent event) {
        List<Guild> guilds = event.getJDA().getGuilds();
        Runtime runtime = Runtime.getRuntime();
        OperatingSystemMXBean bean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        EmbedBuilder builder = new EmbedBuilder();

        String javaVersion = System.getProperty("java.version");
        int guildAmount = guilds.size();
        int userAmount = guilds.stream().mapToInt(Guild::getMemberCount).sum();
        long memoryUsed = runtime.totalMemory() - runtime.freeMemory();
        double cpuUsage = bean.getProcessCpuLoad();
        long uptime = ManagementFactory.getRuntimeMXBean().getUptime();
        String jdaVersion = JDAInfo.VERSION;

        builder.addField("Java version", javaVersion, true);

        builder.addField("Total guilds", String.valueOf(guildAmount), true);

        builder.addField("Total users", String.valueOf(userAmount), true);

        builder.addField("RAM Usage", memoryUsed / 1000000 + "M", true);

        builder.addField("CPU Usage", Math.round(cpuUsage) + "%", true);

        builder.addField("Uptime", uptime / 1000 / 60 + " Minutes", true);

        builder.addField("JDA Version", jdaVersion, true);

        event.getChannel().sendMessage(builder.build()).queue();
    }

    @Override
    public String getInvoke() {
        return "botinfo";
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
        return "Shows information about the bot!";
    }
}
