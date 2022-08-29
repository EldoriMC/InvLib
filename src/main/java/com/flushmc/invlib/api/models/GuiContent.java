package com.flushmc.invlib.api.models;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class GuiContent {

    @Getter
    List<GuiItem> itens = new ArrayList<>();

    public void add(GuiItem item) {
        itens.stream()
                .filter(i -> i.getSlot() == item.getSlot()).findFirst()
                .ifPresent(i -> itens.remove(i));
        itens.add(item);
    }

    public void update(int slot, ItemStack itemStack) {
        update(slot, itemStack, null);
    }

    public void update(int slot, ItemStack itemStack, Consumer<GuiAction> consumer) {
        var item = itens.stream()
                .filter(i -> i.getSlot() == slot).findFirst()
                .orElse(null);

        if (item != null) {
            item.setItem(itemStack);
            if (consumer != null) {
                item.setConsumer(consumer);
            }
        }
    }

    public void reset() {
        itens.clear();
    }

    public void remove(int slot) {
        itens.stream()
                .filter(i -> i.getSlot() == slot).findFirst()
                .ifPresent(itens::remove);
    }

    public GuiItem getItem(int slot) {
        return itens.stream()
                .filter(i -> i.getSlot() == slot).findFirst()
                .orElse(null);
    }

    public GuiContent merge(GuiContent guiContent) {
        ArrayList<GuiItem> temp = new ArrayList<>(itens);
        guiContent.getItens().forEach(item -> {
            temp.stream()
                    .filter(i -> i.getSlot() == item.getSlot()).findFirst()
                    .ifPresent(temp::remove);

            temp.add(item);
        });

        itens.clear();
        itens.addAll(temp);
        return this;
    }

}
