package ru.araclecore.battlecore.connection.profile;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import ru.araclecore.battlecore.connection.Connection;
import ru.araclecore.battlecore.connection.connection.Result;
import ru.araclecore.battlecore.connection.connection.Statement;
import ru.araclecore.battlecore.connection.utilities.Logger;
import ru.araclecore.battlecore.connection.utilities.Utilities;

import java.util.Objects;

public class Data {
    public String uuid;
    public String username;
    public String address;
    public Profile profile;
    public Hero hero;
    private String status;
    private String server;
    private final JavaPlugin instance;

    public Data(JavaPlugin instance, Player player) {
        this.instance = instance;
        String uuid = player.getUniqueId().toString();
        String username = player.getName();
        String address = Objects.requireNonNull(player.getAddress()).getHostName();
        if (!exists(uuid)) {
            if (!Utilities.connection()) return;
            Logger.info(instance, "Creating new data for player. Username: " + player.getName() + ". UUID: " + uuid);
            Statement statement = new Statement(instance);
            statement.table("BCD");
            statement.value(uuid);
            statement.value(username);
            statement.value(address);
            statement.value("online");
            statement.value(Connection.server);
            Connection.database.set(statement.insert());
        }
        if (!Utilities.connection()) return;
        Statement statement = new Statement(instance);
        statement.table("BCD");
        statement.selection("username,address,status,server");
        statement.condition("uuid", uuid);
        Result result = Connection.database.get(statement.select());
        this.uuid = uuid;
        this.username = result.String("username");
        this.address = result.String("address");
        this.status = result.String("status");
        this.server = result.String("server");
        update(username, address);
        this.profile = new Profile(instance, uuid);
        this.hero = new Hero(instance, profile.hero);
    }

    private void update(String username, String address) {
        if (!Utilities.connection()) return;
        if (!this.username.equals(username)) {
            Statement statement = new Statement(instance);
            statement.table("BCD");
            statement.modifier("username", username);
            statement.condition("uuid", uuid);
            Connection.database.set(statement.update());
            this.username = username;
        }

        if (!this.address.equals(address)) {
            if (!Utilities.connection()) return;
            Statement statement = new Statement(instance);
            statement.table("BCD");
            statement.modifier("address", address);
            statement.condition("uuid", uuid);
            Connection.database.set(statement.update());
            this.address = address;
        }

        if (!this.status.equals("online")) {
            if (!Utilities.connection()) return;
            Statement statement = new Statement(instance);
            statement.table("BCD");
            statement.modifier("status", "online");
            statement.condition("uuid", uuid);
            Connection.database.set(statement.update());
            this.status = "online";
        }

        if (!this.server.equals(Connection.server)) {
            if (!Utilities.connection()) return;
            Statement statement = new Statement(instance);
            statement.table("BCD");
            statement.modifier("server", Connection.server);
            statement.condition("uuid", uuid);
            Connection.database.set(statement.update());
            this.server = Connection.server;
        }
    }

    private boolean exists(String uuid) {
        if (!Utilities.connection()) return false;
        Statement statement = new Statement(instance);
        statement.table("BCD");
        statement.selection("uuid");
        statement.condition("uuid", uuid);
        Result result = Connection.database.get(statement.select());
        if (!result.String("uuid").equals("none")) return true;
        Logger.info(instance, "Data was not found. UUID: " + uuid);
        return false;
    }
}
