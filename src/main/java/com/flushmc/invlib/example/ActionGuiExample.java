package com.flushmc.invlib.example;

import com.flushmc.invlib.api.SimpleGui;
import com.flushmc.invlib.api.interfaces.IGuiConfig;
import com.flushmc.invlib.api.models.ActionSlot;
import com.flushmc.invlib.api.models.GuiContent;
import com.flushmc.invlib.api.models.GuiItem;
import com.flushmc.invlib.api.models.config.GuiConfig;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ActionGuiExample extends SimpleGui {

    public ActionGuiExample() {
        super("&lEnchantments", () -> {
            var config = new GuiConfig();
            config.setFillItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
            config.setBlockedBottomInventory(false);
            config.setRestoreActionItensOnClose(true);
            return config;
        });
        setActionSlots(
                ActionSlot.of(13)
        );
    }

    @Override
    public void onBuild(Player player, IGuiConfig iConfig, GuiContent content) {
        content.add(
                new GuiItem(
                        new ItemStack(Material.GRAY_DYE),
                        11, null
                )
        );
        content.add(
                new GuiItem(
                        new ItemStack(Material.GRAY_DYE),
                        15, null
                )
        );
    }

    @Override
    public void onUpdate(Player player, IGuiConfig iConfig, GuiContent content) {
        var action = getActionSlot(13);
        if (action.getItem() == null) {

            content.add(
                    new GuiItem(
                            new ItemStack(Material.GRAY_DYE),
                            11, null
                    )
            );
            content.add(
                    new GuiItem(
                            new ItemStack(Material.GRAY_DYE),
                            15, null
                    )
            );
        } else {
            var item = action.getItem();
            if (item.getType() == Material.DIAMOND_PICKAXE) {

                content.add(
                        new GuiItem(
                                new ItemStack(Material.EXPERIENCE_BOTTLE),
                                11, null
                        )
                );
                content.add(
                        new GuiItem(
                                new ItemStack(Material.EXPERIENCE_BOTTLE),
                                15, null
                        )
                );
            } else {
                content.add(
                        new GuiItem(
                                new ItemStack(Material.ENDER_PEARL),
                                11, null
                        )
                );
                content.add(
                        new GuiItem(
                                new ItemStack(Material.ENDER_PEARL),
                                15, null
                        )
                );
            }
        }
    }
}
