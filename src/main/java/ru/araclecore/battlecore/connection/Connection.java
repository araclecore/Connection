package ru.araclecore.battlecore.connection;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import ru.araclecore.battlecore.connection.connection.Database;
import ru.araclecore.battlecore.connection.connection.Statement;
import ru.araclecore.battlecore.connection.utilities.Configuration;
import ru.araclecore.battlecore.connection.utilities.Manager;

public final class Connection extends JavaPlugin {

    public static Connection instance;
    public static Configuration settings;
    public static String server;
    public static Database database;

    public static Manager manager;

    @Override
    public void onEnable() {
        instance = this;
        settings = new Configuration(instance, "settings.yml");
        server = settings.String("Server");
        manager = new Manager(instance);
        initialize();
    }

    private void initialize() {
        BCD();
        BCP();
        BCH();
    }

    private void BCD() {
        Statement statement = new Statement(instance);
        statement.table("BCD");
        statement.column("uuid", "VARCHAR(36)");
        statement.column("username", "VARCHAR(16)");
        statement.column("address", "VARCHAR(16)");
        statement.column("status", "VARCHAR(7)");
        statement.column("server", "VARCHAR(16)");
        statement.key("uuid");
        database.set(statement.create(Statement.KEY.ENABLE));
    }

    private void BCP() {
        Statement statement = new Statement(instance);
        statement.table("BCP");
        statement.column("uuid", "VARCHAR(36)");
        statement.column("hero", "VARCHAR(36)");
        statement.column("wins", "INT(6)");
        statement.column("defeats", "INT(6)");
        statement.column("kills", "INT(6)");
        statement.column("deaths", "INT(6)");
        statement.column("rating", "INT(6)");
        statement.column("level", "INT(6)");
        statement.column("rang", "INT(6)");
        statement.column("coins", "INT(7)");
        statement.column("crystals", "INT(7)");
        statement.column("trophies", "TEXT");
        statement.column("heroes", "TEXT");
        statement.key("uuid");
        database.set(statement.create(Statement.KEY.ENABLE));
    }

    private void BCH() {
        Statement statement = new Statement(instance);
        statement.table("BCH");
        statement.column("id", "VARCHAR(36)");
        statement.column("hero", "VARCHAR(36)");
        statement.column("suit", "VARCHAR(36)");
        statement.column("ability", "VARCHAR(36)");
        statement.column("wins", "INT(6)");
        statement.column("defeats", "INT(6)");
        statement.column("kills", "INT(6)");
        statement.column("deaths", "INT(6)");
        statement.column("rating", "INT(6)");
        statement.column("level", "INT(6)");
        statement.column("rang", "INT(6)");
        statement.column("suits", "TEXT");
        statement.column("abilities", "TEXT");
        statement.key("id");
        database.set(statement.create(Statement.KEY.ENABLE));
    }

    @Override
    public void onDisable() {
        for (Player player : getServer().getOnlinePlayers()) {
            if (manager.profile(player) != null) manager.profile(player).update();
            if (manager.hero(player) != null) manager.hero(player).update();
        }
        database.close();
    }
}
