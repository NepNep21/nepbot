package me.nepnep.nepbot.json;

import java.util.List;

public class BottomWhiteList {

    private static final String PATH = "whitelist/bottom.json";

    public static List<Long> getList() {
        List<Long> channels = Util.readListFromJsonArray(PATH);

        return channels;
    }
    public static void add(long channel) {
        List<Long> channels = Util.readListFromJsonArray(PATH);

        if (!channels.contains(channel)) {
            channels.add(channel);

            Util.writeListToJsonArray(channels, PATH);
        }
    }
    public static void remove(long channel) {
        List<Long> channels = Util.readListFromJsonArray(PATH);

        if (channels.contains(channel)) {
            channels.remove(channel);

            Util.writeListToJsonArray(channels, PATH);
        }
    }
}
