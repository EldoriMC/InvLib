package com.flushmc.invlib.api;

import com.flushmc.invlib.api.interfaces.IGui;
import com.flushmc.invlib.api.interfaces.IGuiConfig;
import com.flushmc.invlib.api.listeners.GuiListener;
import com.flushmc.invlib.api.models.GuiContent;
import com.flushmc.invlib.api.models.config.GuiConfig;
import com.flushmc.invlib.shared.AsyncTask;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public abstract class SimpleGui implements IGui {

    @Getter private IGuiConfig config;
    @Getter private GuiContent content;
    @Getter private String title;
    private int id;
    private BukkitTask task;
    private Listener guiEvent;
    private AtomicReference<Inventory> inventory;
    @Getter private Player player;

    public SimpleGui(String title, Supplier<GuiConfig> configCallback) {
        this.title = ChatColor.translateAlternateColorCodes('&', title == null ? "" : title);
        this.config = configCallback.get();
        this.content = new GuiContent();
        this.inventory = new AtomicReference<>();
    }

    @Override
    public void open(Player player) {
        this.player = player;

        inventory.set(buildSkeleton(player));
        fillInventoryWithFillItem(inventory.get());

        // Build page using the contents;
        onBuild(player, getConfig(), getContent());
        getContent().getItens().forEach(item -> inventory.get().setItem(item.getSlot(), item.getItem()));

        // Start a update engine for this Gui
        if (getConfig().getInterval() > 0) {
            task = AsyncTask.asyncRepeatingTask(
                    InvLib.getJavaPlugin(),
                    getConfig().getInterval(),
                    getConfig().getInterval(),
                    () -> {
                        onUpdate(player, getConfig(), getContent());
                        fillInventoryWithFillItem(inventory.get());
                        getContent().getItens().forEach(item -> inventory.get().setItem(item.getSlot(), item.getItem()));
                        player.updateInventory();
                    }
            );
        }
        guiEvent = new GuiListener(id, this);
        Bukkit.getPluginManager().registerEvents(guiEvent, InvLib.getJavaPlugin());

        // Play sound if want
        if (getConfig().getSound() != null) {
            player.playSound(
                    player.getLocation(),
                    getConfig().getSound(),
                    1,
                    1
            );
        }

        try {
            player.openInventory(inventory.get());
        } catch (Exception e) {
            InvLib.getAsyncInventoryInteraction().open(player, this);
        }
    }

    @Override
    public void close(Player player) {
        if (guiEvent != null) {
            HandlerList.unregisterAll(guiEvent);
        }
        if (task != null && !task.isCancelled()) {
            task.cancel();
        }
        try {
            player.closeInventory();
        } catch (Exception e) {
            InvLib.getAsyncInventoryInteraction().close(player);
        }
    }

    @Override
    public void onUpdate(Player player, IGuiConfig iConfig, GuiContent content) {
    }

    @Override
    public abstract void onBuild(Player player, IGuiConfig iConfig, GuiContent content);

    @Override
    public void onClose(Player player) {
        if (guiEvent != null) {
            HandlerList.unregisterAll(guiEvent);
        }
        if (task != null && !task.isCancelled()) {
            task.cancel();
        }
    }

    public void refresh() {
        if (player != null && player.isOnline()) {
            onUpdate(player, getConfig(), getContent());
            fillInventoryWithFillItem(inventory.get());
            getContent().getItens().forEach(item -> inventory.get().setItem(item.getSlot(), item.getItem()));
            player.updateInventory();
        }
    }

    private Inventory buildSkeleton(Player player) {
        var inv = Bukkit.createInventory(player, 9 * getConfig().getRows(), getTitle());
        this.id = inv.hashCode();
        return inv;
    }

    private void fillInventoryWithFillItem(Inventory inventory) {
        if (getConfig().getFillItem() != null) {
            for (int i = 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, getConfig().getFillItem());
            }
        }
    }

}
