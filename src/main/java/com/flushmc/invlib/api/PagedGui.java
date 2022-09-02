package com.flushmc.invlib.api;

import com.flushmc.invlib.api.interfaces.IGui;
import com.flushmc.invlib.api.interfaces.IGuiConfig;
import com.flushmc.invlib.api.listeners.GuiListener;
import com.flushmc.invlib.api.models.GuiContent;
import com.flushmc.invlib.api.models.GuiItem;
import com.flushmc.invlib.api.models.config.PagedGuiConfig;
import com.flushmc.invlib.shared.AsyncTask;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public abstract class PagedGui implements IGui {

    @Getter private PagedGuiConfig config;
    @Getter private GuiContent content;
    @Getter private String title;
    @Getter @Setter List<ItemStack> itens;
    private int id, page = 1;
    private BukkitTask task;
    private Listener guiEvent;
    private AtomicReference<Inventory> inventory;
    @Getter private Player player;

    public PagedGui(String title, Supplier<PagedGuiConfig> configCallback) {
        this.title = ChatColor.translateAlternateColorCodes('&', title == null ? "" : title);
        this.config = configCallback.get();
        this.content = new GuiContent();
        this.itens = new ArrayList<>();
        this.inventory = new AtomicReference<>();
    }

    @Override
    public void open(Player player) {
        this.player = player;

        inventory.set(buildSkeleton(player));
        fillInventoryWithFillItem(inventory.get());

        // Build page using the contents;
        onBuild(player, getConfig(), getContent());
        updateGui();
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
                        updateGui();
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
    public void onClose() {
        if (guiEvent != null) {
            HandlerList.unregisterAll(guiEvent);
        }
        if (task != null && !task.isCancelled()) {
            task.cancel();
        }
    }

    @Override
    public abstract void onBuild(Player player, IGuiConfig iConfig, GuiContent content);

    public abstract void onClick(Player player, ItemStack item, ClickType clickType);

    public void refresh() {
        if (player != null && player.isOnline()) {
            onUpdate(player, getConfig(), getContent());
            fillInventoryWithFillItem(inventory.get());
            updateGui();
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

    // Page Navigation
    private void next() {
        int max = getItens().size() / getConfig().getSlots().size();
        int rest = getItens().size() % getConfig().getSlots().size();
        if (rest != 0) max ++;
        if (page + 1 <= max) {
            page++;
            updateGui();
        }
    }

    private void previous() {
        if (page -1 > 0) {
            page--;
            updateGui();
        }
    }

    private void updateGui() {
        var ipp = getConfig().getSlots().size();
        int start = page * ipp - ipp;
        int end = page * ipp;
        int max = getItens().size() / ipp;
        int rest = getItens().size() % ipp;
        if (rest != 0) max++;
        int slot = 0;

        getConfig().getSlots().forEach(s -> getContent().remove(s));

        for (int i = start; i < end; i++) {
            if (i < getItens().size()) {
                getContent().add(
                        new GuiItem(
                                getItens().get(i),
                                getConfig().getSlots().get(slot),
                                action -> onClick(action.getPlayer(), action.getItem(), action.getClickType())
                        )
                );
            } else {
                if (getConfig().getFillItem() != null && getConfig().getFillItem().getType() != Material.AIR) {
                    getContent().add(
                            new GuiItem(
                                    getConfig().isCompleteEmptySlotsIfFillItemIsNotNull() ? getConfig().getFillItem() : new ItemStack(Material.AIR),
                                    getConfig().getSlots().get(slot),
                                    null
                            )
                    );
                } else {
                    getContent().add(
                            new GuiItem(
                                    new ItemStack(Material.AIR),
                                    getConfig().getSlots().get(slot),
                                    null
                            )
                    );
                }
            }
            slot++;
        }

        getContent().add(
                new GuiItem(
                        getConfig().getBackItem(),
                        getConfig().getBackSlot(),
                        action -> {
                            previous();
                            getContent().getItens().forEach(item -> action.getInventory().setItem(item.getSlot(), item.getItem()));
                            action.getPlayer().updateInventory();
                        }
                )
        );

        getContent().add(
                new GuiItem(
                        getConfig().getNextItem(),
                        getConfig().getNextSlot(),
                        action -> {
                            next();
                            getContent().getItens().forEach(item -> action.getInventory().setItem(item.getSlot(), item.getItem()));
                            action.getPlayer().updateInventory();
                        }
                )
        );

        if (getConfig().getFillItem() != null && getConfig().getFillItem().getType() != Material.AIR) {
            if (page == 1) {
                getContent().remove(getConfig().getBackSlot());
            }
            if (page == max || getItens().size() < getConfig().getSlots().size()) {
                getContent().remove(getConfig().getNextSlot());
            }
        } else {
            if (page == 1) {
                getContent().update(getConfig().getBackSlot(), getConfig().getFillItem());
            }
            if (page == max || getItens().size() < getConfig().getSlots().size()) {
                getContent().update(getConfig().getNextSlot(), getConfig().getFillItem());
            }
        }

    }

}
