package me.konoplanyy.soulshard.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.*;

public class InventoryHistorySavedData extends SavedData {

    private static final String NAME = "soulshard_inventory_history";
    private static final int MAX_SNAPSHOTS = 5;

    private final Map<UUID, Deque<InventorySnapshot>> history = new HashMap<>();

    public static InventoryHistorySavedData get(ServerLevel level) {
        return level.getServer()
                .overworld()
                .getDataStorage()
                .computeIfAbsent(InventoryHistorySavedData::load, InventoryHistorySavedData::new, NAME);
    }

    public void addSnapshot(UUID playerUuid, InventorySnapshot snapshot) {
        Deque<InventorySnapshot> snapshots = history.computeIfAbsent(playerUuid, k -> new ArrayDeque<>());
        snapshots.addFirst(snapshot);
        if (snapshots.size() > MAX_SNAPSHOTS)
            snapshots.removeLast();
        setDirty();
    }

    public List<InventorySnapshot> getSnapshots(UUID playerUuid) {
        return new ArrayList<>(history.getOrDefault(playerUuid, new ArrayDeque<>()));
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        CompoundTag playersTag = new CompoundTag();
        history.forEach((uuid, snapshots) -> {
            ListTag snapshotsTag = new ListTag();
            snapshots.forEach(snapshot -> snapshotsTag.add(snapshot.save()));
            playersTag.put(uuid.toString(), snapshotsTag);
        });
        tag.put("players", playersTag);
        return tag;
    }

    private static InventoryHistorySavedData load(CompoundTag tag) {
        InventoryHistorySavedData data = new InventoryHistorySavedData();
        CompoundTag playersTag = tag.getCompound("players");

        for (String key : playersTag.getAllKeys()) {
            UUID uuid = UUID.fromString(key);
            ListTag snapshotsTag = playersTag.getList(key, Tag.TAG_COMPOUND);
            Deque<InventorySnapshot> snapshots = new ArrayDeque<>();
            for (int i = 0; i < snapshotsTag.size(); i++)
                snapshots.addLast(InventorySnapshot.load(snapshotsTag.getCompound(i)));
            data.history.put(uuid, snapshots);
        }

        return data;
    }
}