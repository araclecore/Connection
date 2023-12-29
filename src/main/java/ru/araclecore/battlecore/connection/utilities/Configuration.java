package ru.araclecore.battlecore.connection.utilities;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Configuration {
    private final File file;
    private final FileConfiguration configuration;
    private final JavaPlugin instance;
    private final String filename;

    public Configuration(JavaPlugin instance, String filename) {
        this.file = new File(instance.getDataFolder(), filename);
        this.configuration = YamlConfiguration.loadConfiguration(file);
        this.instance = instance;
        this.filename = filename;
        create();
    }

    public void create() {
        if (!file.exists()) {
            try {
                instance.saveResource(filename, true);
                configuration.load(file);
            } catch (IOException | InvalidConfigurationException exception) {
                Logger.error(instance, exception.getMessage());
            }
        }
    }

    public FileConfiguration configuration() {
        return configuration;
    }

    public Integer Integer(String path) {
        return configuration.getInt(path);
    }

    public boolean Boolean(String path) {
        return configuration.getBoolean(path);
    }

    public String String(String path) {
        return configuration.getString(path);
    }

    public List<String> Strings(String path) {
        return configuration.getStringList(path);
    }
}
