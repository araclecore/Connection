package ru.araclecore.battlecore.connection.utilities;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class Configuration {
    private FileConfiguration configuration;
    private final String filename;
    private final String token;

    public Configuration(JavaPlugin instance, String filename, String token) {
        this.token = token;
        this.filename = filename;
        try {
            File file = new File(filename);
            this.configuration = YamlConfiguration.loadConfiguration(file);
            configuration.loadFromString(content());
        } catch (InvalidConfigurationException exception) {
            Logger.error(instance, exception.getMessage());
        }
    }

    private String content() {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://raw.githubusercontent.com/araclecore/BattleCore/master/" + this.filename)
                )
                .GET()
                .setHeader("Authorization", "token %s".formatted(token))
                .build();

        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return response.body();
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
