package com.flushmc.invlib.api.interfaces;

import com.flushmc.invlib.api.models.GuiContent;
import org.bukkit.entity.Player;

public interface IGui {

    IGuiConfig getConfig();
    GuiContent getContent();
    String getTitle();
    void open(Player player);
    void close(Player player);
    void refresh();

    void onBuild(Player player, IGuiConfig iConfig, GuiContent content);
    void onUpdate(Player player, IGuiConfig iConfig, GuiContent content);
    void onClose();

}
