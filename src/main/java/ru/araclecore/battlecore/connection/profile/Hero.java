package ru.araclecore.battlecore.connection.profile;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import ru.araclecore.battlecore.connection.Connection;
import ru.araclecore.battlecore.connection.connection.Result;
import ru.araclecore.battlecore.connection.connection.Statement;
import ru.araclecore.battlecore.connection.utilities.Logger;
import ru.araclecore.battlecore.connection.utilities.Utilities;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Hero {

    public String id;
    public String hero;
    public String suit;
    public String ability;
    public int wins;
    public int defeats;
    public int kills;
    public int deaths;
    public int rating;
    public int level;
    public int rang;
    public String suits;
    public String abilities;
    private final JavaPlugin instance;

    public Hero(JavaPlugin instance, String id) {
        this.instance = instance;
        if (!exists(id)) {
            if (!Utilities.connection()) return;
            Logger.info(instance, "Creating new hero. UUID: " + id);
            create("warrior", "CommonWarrior", "berserk", id);
        }
        if (!Utilities.connection()) return;
        Statement statement = new Statement(instance);
        statement.table("BCH");
        statement.selection("hero,suit,ability,wins,defeats,kills,deaths,rating,level,rang,suits,abilities");
        statement.condition("id", id);
        Result result = Connection.database.get(statement.select());
        this.id = id;
        this.hero = result.String("hero");
        this.suit = result.String("suit");
        this.ability = result.String("ability");
        this.wins = result.Integer("wins");
        this.defeats = result.Integer("defeats");
        this.kills = result.Integer("kills");
        this.deaths = result.Integer("deaths");
        this.rating = result.Integer("rating");
        this.level = result.Integer("level");
        this.rang = result.Integer("rang");
        this.suits = result.String("suits");
        this.abilities = result.String("abilities");
    }

    public void hero(String hero, boolean update) {
        this.hero = hero;
        if (update) update("hero", hero);
    }

    public void suit(String suit, boolean update) {
        this.suit = suit;
        if (update) update("suit", suit);
    }

    public void ability(String ability, boolean update) {
        this.ability = ability;
        if (update) update("ability", ability);
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

    public void suits(String suits, boolean update) {
        this.suits = suits;
        if (update) update("suits", suits);
    }

    public void abilities(String abilities, boolean update) {
        this.abilities = abilities;
        if (update) update("abilities", abilities);
    }

    public List<String> suits(Player player) {
        return Arrays.stream(suits.split(",")).toList();
    }

    public List<String> abilities(Player player) {
        return Arrays.stream(abilities.split(",")).toList();
    }

    public void update(String column, String value) {
        if (!Utilities.connection()) return;
        Statement statement = new Statement(instance);
        statement.table("BCH");
        statement.modifier(column, value);
        statement.condition("id", id);
        Connection.database.set(statement.update());
    }

    public void update() {
        if (!Utilities.connection()) return;
        Statement statement = new Statement(instance);
        statement.table("BCH");
        statement.modifier("hero", hero);
        statement.modifier("suit", suit);
        statement.modifier("ability", ability);
        statement.modifier("wins", String.valueOf(wins));
        statement.modifier("defeats", String.valueOf(defeats));
        statement.modifier("kills", String.valueOf(kills));
        statement.modifier("deaths", String.valueOf(deaths));
        statement.modifier("rating", String.valueOf(rating));
        statement.modifier("level", String.valueOf(level));
        statement.modifier("rang", String.valueOf(rang));
        statement.modifier("suits", suits);
        statement.modifier("abilities", abilities);
        statement.condition("id", id);
        Connection.database.set(statement.update());
    }

    private boolean exists(String id) {
        if (!Utilities.connection()) return false;
        Statement statement = new Statement(instance);
        statement.table("BCH");
        statement.selection("id");
        statement.condition("id", id);
        Result result = Connection.database.get(statement.select());
        if (!result.String("id").equals("none")) return true;
        Logger.info(instance, "Hero was not found. ID: " + id);
        return false;
    }

    public static String create(String name, String suit, String ability, @Nullable String id) {
        if (id == null) id = UUID.randomUUID().toString();
        if (!Utilities.connection()) return id;
        Logger.info(Connection.instance, "Creating new hero. UUID: " + id);
        Statement statement = new Statement(Connection.instance);
        statement.table("BCH");
        statement.value(id);
        statement.value(name);
        statement.value(suit);
        statement.value(ability);
        statement.value("0");
        statement.value("0");
        statement.value("0");
        statement.value("0");
        statement.value("0");
        statement.value("0");
        statement.value("0");
        statement.value(suit);
        statement.value(ability);
        Connection.database.set(statement.insert());
        return id;
    }
}
