package com.flushmc.invlib.example;

import com.flushmc.invlib.api.interfaces.IGuiConfig;
import com.flushmc.invlib.api.models.GuiContent;
import com.flushmc.invlib.api.models.GuiItem;
import com.flushmc.invlib.api.SimpleGui;
import com.flushmc.invlib.api.models.config.GuiConfig;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;

public class SimpleGuiExample extends SimpleGui {

    List<Material> glasses = List.of(
            Material.BLACK_STAINED_GLASS_PANE,
            Material.LIME_STAINED_GLASS_PANE,
            Material.YELLOW_STAINED_GLASS_PANE,
            Material.PINK_STAINED_GLASS_PANE,
            Material.RED_STAINED_GLASS_PANE,
            Material.PURPLE_STAINED_GLASS_PANE,
            Material.CYAN_STAINED_GLASS_PANE
    );

    public SimpleGuiExample() {
        super("&lTeste", () -> {
            var config = new GuiConfig();
            config.setFillItem(new ItemStack(Material.LIME_STAINED_GLASS_PANE));
            config.setSound(Sound.BLOCK_CHEST_OPEN);
            config.setInterval(5);
            config.setBlockedBottomInventory(false);
            return config;
        });
    }

    @Override
    public void onBuild(IGuiConfig iConfig, GuiContent content) {
        content.add(
                new GuiItem(
                        new ItemStack(Material.PAINTING),
                        10,
                        (action) -> {
                            new PagedGuiExample().open(action.getPlayer());
                        }
                )
        );
        content.add(
                new GuiItem(
                        new ItemStack(Material.IRON_AXE),
                        12,
                        (action) -> {
                            action.getPlayer().sendMessage("FERROOOO");
                        }
                )
        );
    }

    @Override
    public void onUpdate(IGuiConfig iConfig, GuiContent content) {
        var random = new Random();
        var config = (GuiConfig) iConfig;
        config.setFillItem(new ItemStack(glasses.get(random.nextInt(glasses.size()))));
        content.add(
                new GuiItem(
                        new ItemStack(Material.values()[random.nextInt(Material.values().length)]),
                        14,
                        (action) -> {
                        }
                )
        );
        content.add(
                new GuiItem(
                        new ItemStack(Material.values()[random.nextInt(Material.values().length)]),
                        16,
                        (action) -> {
                        }
                )
        );
    }
}
