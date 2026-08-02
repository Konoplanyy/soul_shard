package me.konoplanyy.soulshard.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public record ShardRecord(UUID id, UUID ownerUUID, long createdAt, NonNullList<ItemStack> items) {
    public CompoundTag save(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        tag.putUUID("Id", id);
        tag.putUUID("Owner", ownerUUID);
        tag.putLong("CreatedAt", createdAt);
        ContainerHelper.saveAllItems(tag, items, registries);
        return tag;
    }

    public static ShardRecord load(CompoundTag tag, HolderLookup.Provider registries) {
        UUID id = tag.getUUID("Id");
        UUID ownerUUID = tag.getUUID("Owner");
        long createdAt = tag.getLong("CreatedAt");
        NonNullList<ItemStack> items = NonNullList.withSize(41, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, items, registries);
        return new ShardRecord(id, ownerUUID, createdAt, items);
    }
}
