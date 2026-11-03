package me.konoplanyy.soulshard.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public record InventorySnapshot(List<ItemStack> items, long timestamp) {

    public static InventorySnapshot fromPlayer(Player player) {
        List<ItemStack> items = new ArrayList<>(41);
        var inv = player.getInventory();

        for (int i = 0; i < 36; i++)
            items.add(inv.items.get(i).copy());
        for (int i = 0; i < 4; i++)
            items.add(inv.armor.get(i).copy());
        items.add(inv.offhand.get(0).copy());

        return new InventorySnapshot(items, System.currentTimeMillis());
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putLong("timestamp", timestamp);

        ListTag itemsTag = new ListTag();
        for (int i = 0; i < items.size(); i++) {
            ItemStack stack = items.get(i);
            if (!stack.isEmpty()) {
                CompoundTag itemTag = new CompoundTag();
                itemTag.putInt("slot", i);
                stack.save(itemTag);
                itemsTag.add(itemTag);
            }
        }
        tag.put("items", itemsTag);
        return tag;
    }

    public static InventorySnapshot load(CompoundTag tag) {
        long timestamp = tag.getLong("timestamp");

        List<ItemStack> items = new ArrayList<>(Collections.nCopies(41, ItemStack.EMPTY));
        ListTag itemsTag = tag.getList("items", Tag.TAG_COMPOUND);

        for (int i = 0; i < itemsTag.size(); i++) {
            CompoundTag itemTag = itemsTag.getCompound(i);
            int slot = itemTag.getInt("slot");
            if (slot >= 0 && slot < items.size()) {
                items.set(slot, ItemStack.of(itemTag));
            }
        }

        return new InventorySnapshot(items, timestamp);
    }
}