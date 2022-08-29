package com.flushmc.invlib.api.models.config;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PagedGuiConfig extends GuiConfig {

    @Getter @Setter private int backSlot = 0;
    @Getter @Setter private int nextSlot = 8;
    @Getter @Setter private List<Integer> slots = new ArrayList<>();
    @Getter @Setter private ItemStack backItem = new ItemStack(Material.GRAY_DYE);
    @Getter @Setter private ItemStack nextItem = new ItemStack(Material.LIME_DYE);
    @Getter @Setter private boolean completeEmptySlotsIfFillItemIsNotNull = true;

}
