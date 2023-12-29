package ru.araclecore.battlecore.connection.utilities;

import ru.araclecore.battlecore.connection.Connection;

public class Utilities {

    public static boolean connection() {
        if (Connection.database.connection != null) return true;
        Logger.warn(Connection.instance, "Database connection failed");
        return false;
    }
}
