package com.flushmc.invlib.api.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

@AllArgsConstructor
public class GuiItem {

    @Getter @Setter private ItemStack item;
    @Getter private int slot;
    @Getter @Setter private Consumer<GuiAction> consumer;

}
