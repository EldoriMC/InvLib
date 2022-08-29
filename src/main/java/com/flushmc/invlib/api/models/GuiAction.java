package com.flushmc.invlib.api.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class GuiAction {

    @Getter private Player player;
    @Getter private int slot;
    @Getter private Inventory inventory;
    @Getter private ClickType clickType;

    public ItemStack getItem() {
        return getInventory().getItem(getSlot());
    }

}
