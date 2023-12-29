package ru.araclecore.battlecore.connection.connection;

import org.bukkit.plugin.java.JavaPlugin;
import ru.araclecore.battlecore.connection.utilities.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Result {

    private final Map<String, Object> values = new HashMap<>();
    private final JavaPlugin instance;
    private final Database.result result;

    public Result(JavaPlugin instance, Database.result result) {
        this.instance = instance;
        this.result = result;
        if (!result()) return;
        List<String> columns = new ArrayList<>();
        columns.add(result.columns());
        if (result.columns().contains(",")) {
            String[] strings = result.columns().split(",");
            columns = Arrays.stream(strings).toList();
        }
        ResultSet resultSet = result.result();
        try {
            while (resultSet.next()) {
                columns.forEach(column -> {
                    try {
                        if (resultSet.getObject(column) != null) this.values.put(column, resultSet.getObject(column));
                    } catch (SQLException exception) {
                        Logger.warn(instance, exception.getMessage());
                    }
                });
            }
        } catch (SQLException exception) {
            Logger.warn(instance, exception.getMessage());
        }

    }

    public boolean result() {
        if (result != null) return true;
        Logger.warn(instance, "No result values from query.");
        return false;
    }

    public Integer Integer(String column) {
        if (values.containsKey(column)) return Integer.parseInt(values.get(column).toString());
        return 0;
    }

    public String String(String column) {
        if (values.containsKey(column)) return values.get(column).toString();
        return "none";
    }

    public Boolean Boolean(String column) {
        if (values.containsKey(column)) return Boolean.valueOf(values.get(column).toString());
        return null;
    }

    public List<String> Strings(String column) {
        String string = values.get(column).toString();
        if (values.containsKey(column)) return new ArrayList<>(Arrays.stream(string.split(",")).toList());
        return null;
    }

}
