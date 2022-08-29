package com.flushmc.invlib.api.listeners;

import com.flushmc.invlib.api.PagedGui;
import com.flushmc.invlib.api.interfaces.IGui;
import com.flushmc.invlib.api.models.GuiAction;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Arrays;

@RequiredArgsConstructor
public class GuiListener implements Listener {

    @NonNull private int id;
    @NonNull private IGui gui;
    private InventoryAction[] actions = new InventoryAction[]{
            InventoryAction.COLLECT_TO_CURSOR,
            InventoryAction.DROP_ALL_CURSOR,
            InventoryAction.DROP_ALL_SLOT,
            InventoryAction.DROP_ONE_CURSOR,
            InventoryAction.DROP_ONE_SLOT,
            InventoryAction.HOTBAR_SWAP,
            InventoryAction.HOTBAR_MOVE_AND_READD
    };

    @EventHandler
    void onQuit(PlayerQuitEvent e) {
        gui.close(e.getPlayer());
    }

    @EventHandler
    void onClose(InventoryCloseEvent e) {
        if (e.getInventory().hashCode() == id) {
            gui.close((Player) e.getPlayer());
        }
    }

    @EventHandler
    void onClick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player player) {

            var topInventory = e.getView().getTopInventory();
            var clickedInventory = e.getClickedInventory();
            var clickedSlot = e.getRawSlot();

            // Prevent click outside of the window
            if (clickedInventory == null) return;
            var clickedItem = clickedInventory.getItem(clickedSlot);

            if (topInventory.hashCode() == id) {

                // Detect Bottom inventory interract
                if (clickedInventory.hashCode() != id) {
                    if (gui.getConfig().isBlockedBottomInventory()) {
                        e.setCancelled(true);
                    }
                }

                // Cancel other ClickTypes
                if (Arrays.stream(actions).toList().contains(e.getAction()) || e.getClick().isShiftClick()) {
                    e.setCancelled(true);
                }

                // Block interaction on fillItem
                if (gui.getConfig().getFillItem() != null && gui.getConfig().getFillItem().getType() != Material.AIR) {
                    if (clickedItem == gui.getConfig().getFillItem()) {
                        e.setCancelled(true);
                        player.updateInventory();
                    }
                }

                // Detect only Top interractions
                if (clickedInventory.hashCode() == id) {

                    // Block top interract
                    if (gui instanceof PagedGui && gui.getConfig().isBlockedTopInventory()) {
                        e.setCancelled(true);
                    }

                    if (gui.getConfig().isBlockedTopInventory()) {
                        e.setCancelled(true);
                    }

                    var guiItem = gui.getContent().getItens().stream()
                            .filter(i -> i.getSlot() == clickedSlot).findFirst()
                            .orElse(null);

                    // Cancel interraction with item is not null
                    if (guiItem != null && guiItem.getConsumer() != null) {
                        guiItem.getConsumer().accept(
                                new GuiAction(
                                        player,
                                        clickedSlot,
                                        clickedInventory,
                                        e.getClick()
                                )
                        );
                    }

                    e.setCancelled(true);
                }
            }
        }
    }

}
