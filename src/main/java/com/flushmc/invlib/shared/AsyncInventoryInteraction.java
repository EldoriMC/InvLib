package com.flushmc.invlib.shared;

import com.flushmc.invlib.api.interfaces.IGui;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AsyncInventoryInteraction {

    private JavaPlugin plugin;
    private List<Player> players, tempPlayers;
    private HashMap<Player, IGui> guis;
    private BukkitTask task;

    public AsyncInventoryInteraction(JavaPlugin plugin) {
        this.plugin = plugin;
        this.players = new ArrayList<>();
        this.tempPlayers = new ArrayList<>();
        this.guis = new HashMap<>();
        init();
    }

    public void open(Player player, IGui gui) {
        guis.put(player, gui);
    }

    public void close(Player player) {
        players.add(player);
    }

    private void init() {
        task = AsyncTask.syncRepeatingTask(plugin, 1, 1, () -> {
            if (!players.isEmpty()) {
                tempPlayers.addAll(players);

                tempPlayers.forEach(Player::closeInventory);
                tempPlayers.clear();
            }
            if (!guis.isEmpty()) {
                guis.forEach(((player, iGui) -> iGui.open(player)));
                guis.clear();
            }
        });
    }

    public void stop() {
        if (task != null && task.isCancelled()) {
            task.cancel();
        }
    }
}
