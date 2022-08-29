package com.flushmc.invlib.example;

import com.flushmc.invlib.api.PagedGui;
import com.flushmc.invlib.api.interfaces.IGuiConfig;
import com.flushmc.invlib.api.models.GuiContent;
import com.flushmc.invlib.api.models.GuiItem;
import com.flushmc.invlib.api.models.config.GuiConfig;
import com.flushmc.invlib.api.models.config.PagedGuiConfig;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;

public class PagedGuiExample extends PagedGui {

    List<Material> glasses = List.of(
            Material.BLACK_STAINED_GLASS_PANE,
            Material.LIME_STAINED_GLASS_PANE,
            Material.YELLOW_STAINED_GLASS_PANE,
            Material.PINK_STAINED_GLASS_PANE,
            Material.RED_STAINED_GLASS_PANE,
            Material.PURPLE_STAINED_GLASS_PANE,
            Material.CYAN_STAINED_GLASS_PANE
    );


    public PagedGuiExample() {
        super("&lTeste Paginado", () -> {
            PagedGuiConfig config = new PagedGuiConfig();

            config.setBackSlot(18);
            config.setNextSlot(26);
            config.setRows(3);
            config.setSlots(List.of(
                    10, 11, 12, 13, 14, 15, 16
            ));
            config.setFillItem(new ItemStack(Material.LIME_STAINED_GLASS_PANE));
            return config;
        });
    }

    @Override
    public void onBuild(IGuiConfig iConfig, GuiContent content) {
        setItens(
                List.of(
                        new ItemStack(Material.STONE_SWORD),
                        new ItemStack(Material.IRON_PICKAXE),
                        new ItemStack(Material.STONE_SHOVEL),
                        new ItemStack(Material.STONE_AXE),
                        new ItemStack(Material.BREAD, 8),
                        new ItemStack(Material.BEEF, 8),
                        new ItemStack(Material.CHEST, 2),
                        new ItemStack(Material.OAK_LOG, 16)
                )
        );
        content.add(
                new GuiItem(
                        new ItemStack(Material.TNT),
                        22,
                        action -> new SimpleGuiExample().open(action.getPlayer())
                )
        );
    }

    @Override
    public void onUpdate(IGuiConfig iConfig, GuiContent content) {
        var random = new Random();
        var config = (GuiConfig) iConfig;
        config.setFillItem(new ItemStack(glasses.get(random.nextInt(glasses.size()))));
    }

    @Override
    public void onClick(Player player, ItemStack item, ClickType clickType) {
        player.sendMessage(item.getType().name());
    }
}
