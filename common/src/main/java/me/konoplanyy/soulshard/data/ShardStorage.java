package me.konoplanyy.soulshard.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.*;

public class ShardStorage extends SavedData {
    private static final String DATA_NAME = "soulshard_shards";

    private final Map<UUID, ShardRecord> shards = new HashMap<>();

    public static ShardStorage get(ServerLevel level) {
        ServerLevel overworld = level.getServer().getLevel(Level.OVERWORLD);
        return overworld.getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(ShardStorage::new, ShardStorage::load, null),
                DATA_NAME
        );
    }

    public void addShard(ShardRecord shard) {
        shards.put(shard.id(), shard);
        setDirty();
    }

    public ShardRecord getShard(UUID id) {
        return shards.get(id);
    }

    public List<ShardRecord> getShardsByOwner(UUID ownerUUID) {
        return shards.values().stream()
                .filter(shard -> shard.ownerUUID().equals(ownerUUID))
                .sorted(Comparator.comparingLong(ShardRecord::createdAt).reversed())
                .toList();
    }

    public ShardRecord removeShard(UUID id) {
        ShardRecord removed = shards.remove(id);
        if (removed != null) {
            setDirty();
        }
        return removed;
    }

    private static ShardStorage load(CompoundTag tag, HolderLookup.Provider registries) {
        ShardStorage storage = new ShardStorage();
        ListTag ShardsTag = tag.getList("Shards", Tag.TAG_COMPOUND);
        for (int i = 0; i < ShardsTag.size(); i++) {
            ShardRecord Shard = ShardRecord.load(ShardsTag.getCompound(i), registries);
            storage.shards.put(Shard.id(), Shard);
        }
        return storage;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        ListTag shardsTag = new ListTag();
        for (ShardRecord grave : shards.values()) {
            shardsTag.add(grave.save(registries));
        }
        tag.put("Shards", shardsTag);
        return tag;
    }

}
