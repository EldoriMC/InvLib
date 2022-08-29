package com.flushmc.invlib.api.interfaces;

import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

public interface IGuiConfig {

    boolean isBlockedBottomInventory();
    boolean isBlockedTopInventory();
    int getRows();
    int getInterval();
    Sound getSound();
    ItemStack getFillItem();

}
