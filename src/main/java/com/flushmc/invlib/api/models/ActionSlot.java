package com.flushmc.invlib.api.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class ActionSlot {

    @Getter @Setter private ItemStack item;
    @Getter private int slot;

    public static ActionSlot of(int slot) {
        return new ActionSlot(new ItemStack(Material.STRUCTURE_VOID), slot);
    }

}
