package com.flushmc.invlib.shared;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class AsyncTask {

    public static BukkitTask asyncRepeatingTask(Plugin plugin, long delay, long period, Runnable runnable) {
        return new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
            }
        }.runTaskTimerAsynchronously(plugin, delay, period);
    }

    public static BukkitTask syncRepeatingTask(Plugin plugin, long delay, long period, Runnable runnable) {
        return new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
            }
        }.runTaskTimer(plugin, delay, period);
    }

}
