package ru.araclecore.battlecore.connection.utilities;

import com.google.common.collect.Maps;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import ru.araclecore.battlecore.connection.profile.Data;
import ru.araclecore.battlecore.connection.profile.Hero;
import ru.araclecore.battlecore.connection.profile.Profile;

import java.util.Map;

public class Manager implements org.bukkit.event.Listener {

    private final Map<Player, Data> profiles;

    private final JavaPlugin instance;

    public Manager(JavaPlugin instance) {
        this.instance = instance;
        profiles = Maps.newHashMap();
    }


    @EventHandler
    private void join(PlayerJoinEvent event) {
        if (!Utilities.connection()) return;
        Player player = event.getPlayer();
        profiles.put(player, new Data(instance, player));
    }

    @EventHandler
    private void quit(PlayerQuitEvent event) {
        if (!Utilities.connection()) return;
        Player player = event.getPlayer();
        if (!profiles.containsKey(player)) return;
        profile(player).update();
        hero(player).update();
        profiles.remove(player, new Data(instance, player));
    }

    public Data data(JavaPlugin instance, Player player) {
        return new Data(instance, player);
    }

    public Hero hero(JavaPlugin instance, String id) {
        return new Hero(instance, id);
    }

    public Profile profile(JavaPlugin instance, String uuid) {
        return new Profile(instance, uuid);
    }

    public Data data(Player player) {
        if (profiles.containsKey(player)) return profiles.get(player);
        return null;
    }

    public Hero hero(Player player) {
        if (profiles.containsKey(player)) return profiles.get(player).hero;
        return null;
    }

    public Profile profile(Player player) {
        if (profiles.containsKey(player)) return profiles.get(player).profile;
        return null;
    }
}
