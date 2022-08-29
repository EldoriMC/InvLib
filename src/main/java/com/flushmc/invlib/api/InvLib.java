package com.flushmc.invlib.api;

import com.flushmc.invlib.shared.AsyncInventoryInteraction;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public class InvLib {

    @Getter(AccessLevel.PROTECTED)
    private static AsyncInventoryInteraction asyncInventoryInteraction;
    @Getter(AccessLevel.PROTECTED)
    private static JavaPlugin javaPlugin;

    public static void init(JavaPlugin plugin) {
        javaPlugin = plugin;
        asyncInventoryInteraction = new AsyncInventoryInteraction(plugin);
    }

    public static void stop() {
        asyncInventoryInteraction.stop();
    }

}
