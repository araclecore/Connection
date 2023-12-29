package ru.araclecore.battlecore.connection.connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.plugin.java.JavaPlugin;
import ru.araclecore.battlecore.connection.Connection;
import ru.araclecore.battlecore.connection.utilities.Logger;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Database {
    private final String id;
    private final String username;
    private final String password;
    private final String database;
    private final String address;
    private final String port;
    private boolean validated = true;
    private final JavaPlugin instance;

    private void validatedConnection() {
        String url = "jdbc:mysql://" + address + ":" + port + "/" + database;
        try {
            DriverManager.getConnection(url, username, password);
        } catch (SQLException exception) {
            Logger.warn(instance, "Database connection error");
            Logger.warn(instance, exception.getMessage());
            validated = false;
        }
    }

    private void validateData() {
        if (id.equals("") || username.equals("") || password.equals("") || database.equals("") || address.equals("") || port.equals("")) {
            Logger.warn(instance, "Missing data in \"settings.yml\". Check that the \"Connection\" fields are filled in correctly");
            validated = false;
        }
    }

    public java.sql.Connection connection;

    public Database(JavaPlugin instance) {
        this.instance = instance;
        this.id = Connection.settings.String("Connection.connection-id");
        this.username = Connection.settings.String("Connection.username");
        this.password = Connection.settings.String("Connection.password");
        this.database = Connection.settings.String("Connection.database");
        this.address = Connection.settings.String("Connection.address");
        this.port = Connection.settings.String("Connection.port");
        String url = "jdbc:mysql://" + address + ":" + port + "/" + database;
        validateData();
        validatedConnection();
        if (!validated) return;
        HikariConfig config = new HikariConfig();
        config.setPoolName(id);
        config.setUsername(username);
        config.setPassword(password);
        config.setJdbcUrl(url);
        try {
            connection = new HikariDataSource(config).getConnection();
        } catch (SQLException exception) {
            Logger.error(instance, exception.getMessage());
        }
    }

    public void close() {
        if (connection == null) return;
        try {
            connection.close();
        } catch (SQLException exception) {
            Logger.warn(instance, exception.getMessage());
        }
    }

    public void set(String query) {
        if (connection()) return;
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement(query);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException exception) {
            Logger.warn(instance, exception.getMessage());
        }
    }

    public Result get(List<String> query) {
        if (connection()) return null;
        ResultSet resultSet = null;
        try {
            resultSet = connection.prepareStatement(query.get(0)).executeQuery();
        } catch (SQLException exception) {
            Logger.warn(instance, exception.getMessage());
        }
        result result = new result(resultSet, query.get(1));
        return new Result(instance, result);
    }

    public record result(ResultSet result, String columns) {
    }

    public boolean connection() {
        if (connection != null) return false;
        Logger.warn(instance, "No connection to database.");
        return true;
    }


}
