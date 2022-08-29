package com.flushmc.invlib.api.models.config;

import com.flushmc.invlib.api.interfaces.IGuiConfig;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

public class GuiConfig implements IGuiConfig {

    @Getter @Setter private boolean BlockedBottomInventory = true;
    @Getter @Setter private boolean BlockedTopInventory = true;
    @Getter @Setter private int interval = 0;
    @Getter @Setter private String title = "";
    @Getter @Setter private int rows = 3;
    @Getter @Setter private Sound sound;
    @Getter @Setter private ItemStack fillItem;

}
