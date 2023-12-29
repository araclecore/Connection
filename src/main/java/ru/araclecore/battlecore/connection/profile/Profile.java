package ru.araclecore.battlecore.connection.profile;

import org.bukkit.plugin.java.JavaPlugin;
import ru.araclecore.battlecore.connection.Connection;
import ru.araclecore.battlecore.connection.connection.Result;
import ru.araclecore.battlecore.connection.connection.Statement;
import ru.araclecore.battlecore.connection.utilities.Logger;
import ru.araclecore.battlecore.connection.utilities.Utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Profile {

    public String uuid;
    public String hero;
    public int wins;
    public int defeats;
    public int kills;
    public int deaths;
    public int rating;
    public int level;
    public int rang;
    public int coins;
    public int crystals;
    public String trophies;
    public String heroes;
    private final JavaPlugin instance;

    public Profile(JavaPlugin instance, String uuid) {
        this.instance = instance;
        if (!exists(uuid)) {
            if (!Utilities.connection()) return;
            Logger.info(instance, "Creating new profile. UUID: " + uuid);
            String id = UUID.randomUUID().toString();
            Statement statement = new Statement(instance);
            statement.table("BCP");
            statement.value(uuid);
            statement.value(id);
            statement.value("0");
            statement.value("0");
            statement.value("0");
            statement.value("0");
            statement.value("0");
            statement.value("0");
            statement.value("0");
            statement.value("0");
            statement.value("0");
            statement.value("beta");
            statement.value(id + "," + Hero.create("wizard", "CommonWizard", "wave", null)
                    + "," + Hero.create("hunter", "CommonHunter", "trap", null));
            Connection.database.set(statement.insert());
        }
        if (!Utilities.connection()) return;
        Statement statement = new Statement(instance);
        statement.table("BCP");
        statement.selection("hero,wins,defeats,kills,deaths,rating,level,rang,coins,crystals,trophies,heroes");
        statement.condition("uuid", uuid);
        Result result = Connection.database.get(statement.select());
        this.uuid = uuid;
        this.hero = result.String("hero");
        this.wins = result.Integer("wins");
        this.defeats = result.Integer("defeats");
        this.kills = result.Integer("kills");
        this.deaths = result.Integer("deaths");
        this.rating = result.Integer("rating");
        this.level = result.Integer("level");
        this.rang = result.Integer("rang");
        this.coins = result.Integer("coins");
        this.crystals = result.Integer("crystals");
        this.trophies = result.String("trophies");
        this.heroes = result.String("heroes");
    }

    public void hero(String hero, boolean update) {
        this.hero = hero;
        if (update) update("hero", hero);
    }

    public void wins(Integer wins, boolean update) {
        this.wins = wins;
        if (update) update("wins", String.valueOf(wins));
    }

    public void defeats(Integer defeats, boolean update) {
        this.defeats = defeats;
        if (update) update("defeats", String.valueOf(defeats));
    }

    public void kills(Integer kills, boolean update) {
        this.kills = kills;
        if (update) update("kills", String.valueOf(kills));
    }

    public void deaths(Integer deaths, boolean update) {
        this.deaths = deaths;
        if (update) update("deaths", String.valueOf(deaths));
    }

    public void rating(Integer rating, boolean update) {
        this.rating = rating;
        if (update) update("rating", String.valueOf(rating));
    }

    public void level(Integer level, boolean update) {
        this.level = level;
        if (update) update("level", String.valueOf(level));
    }

    public void rang(Integer rang, boolean update) {
        this.rang = rang;
        if (update) update("rang", String.valueOf(rang));
    }

    public void coins(Integer coins, boolean update) {
        this.coins = coins;
        if (update) update("coins", String.valueOf(coins));
    }

    public void crystals(Integer crystals, boolean update) {
        this.crystals = crystals;
        if (update) update("crystals", String.valueOf(crystals));
    }

    public void trophies(String trophies, boolean update) {
        this.trophies = trophies;
        if (update) update("trophies", trophies);
    }

    public void heroes(String heroes, boolean update) {
        this.heroes = heroes;
        if (update) update("heroes", heroes);
    }

    public List<String> heroes() {
        List<String> ids = Arrays.stream(heroes.split(",")).toList();
        List<String> heroes = new ArrayList<>();
        if (!Utilities.connection()) return heroes;
        ids.forEach(id -> {
            Statement statement = new Statement(instance);
            statement.table("BCH");
            statement.selection("hero");
            statement.condition("id", id);
            Result result = Connection.database.get(statement.select());
            heroes.add(result.String("hero"));
        });
        return heroes;
    }

    public void update(String column, String value) {
        if (!Utilities.connection()) return;
        Statement statement = new Statement(instance);
        statement.table("BCP");
        statement.modifier(column, value);
        statement.condition("uuid", uuid);
        Connection.database.set(statement.update());
    }

    public void update() {
        if (!Utilities.connection()) return;
        Statement statement = new Statement(instance);
        statement.table("BCP");
        statement.modifier("hero", hero);
        statement.modifier("wins", String.valueOf(wins));
        statement.modifier("defeats", String.valueOf(defeats));
        statement.modifier("kills", String.valueOf(kills));
        statement.modifier("deaths", String.valueOf(deaths));
        statement.modifier("rating", String.valueOf(rating));
        statement.modifier("level", String.valueOf(level));
        statement.modifier("rang", String.valueOf(rang));
        statement.modifier("coins", String.valueOf(coins));
        statement.modifier("crystals", String.valueOf(crystals));
        statement.modifier("trophies", trophies);
        statement.modifier("heroes", heroes);
        statement.condition("uuid", uuid);
        Connection.database.set(statement.update());
    }

    private boolean exists(String uuid) {
        if (!Utilities.connection()) return false;
        Statement statement = new Statement(instance);
        statement.table("BCP");
        statement.selection("uuid");
        statement.condition("uuid", uuid);
        Result result = Connection.database.get(statement.select());
        if (!result.String("uuid").equals("none")) return true;
        Logger.info(instance, "Profile was not found. UUID: " + uuid);
        return false;
    }
}
