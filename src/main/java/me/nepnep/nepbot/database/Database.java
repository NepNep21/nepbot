package me.nepnep.nepbot.database;

import com.mongodb.ConnectionString;

public class Database {
    private static final String MONGODB_URL = System.getenv("MONGODB_URL");
    protected static final ConnectionString CONNECTION_STRING = new ConnectionString(MONGODB_URL);
}
