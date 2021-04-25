package me.nepnep.nepbot;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class Networking {
    public static int pingServer(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int status = connection.getResponseCode();
            connection.disconnect();
            return status;
        } catch (IOException e) {
            return 404;
        }
    }
}