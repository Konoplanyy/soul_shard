package me.konoplanyy.soulshard.data;

import com.mojang.serialization.Codec;
import me.konoplanyy.soulshard.SoulShardMod;
import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.*;

public class InventoryHistorySavedData extends SavedData {

    private static final String NAME = "inventory_history";
    private static final int MAX_SNAPSHOTS = 5;

    private static final Codec<InventoryHistorySavedData> CODEC = Codec
            .unboundedMap(UUIDUtil.STRING_CODEC, InventorySnapshot.CODEC.listOf())
            .xmap(InventoryHistorySavedData::fromMap, InventoryHistorySavedData::toMap);

    private static final SavedDataType<InventoryHistorySavedData> TYPE = new SavedDataType<>(
            Identifier.fromNamespaceAndPath(SoulShardMod.MOD_ID, NAME),
            InventoryHistorySavedData::new,
            CODEC,
            DataFixTypes.SAVED_DATA_RANDOM_SEQUENCES
    );

    private final Map<UUID, Deque<InventorySnapshot>> history = new HashMap<>();

    public static InventoryHistorySavedData get(ServerLevel level) {
        return level.getServer()
                .overworld()
                .getDataStorage()
                .computeIfAbsent(TYPE);
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

    private Map<UUID, List<InventorySnapshot>> toMap() {
        Map<UUID, List<InventorySnapshot>> map = new HashMap<>();
        history.forEach((uuid, snapshots) -> map.put(uuid, new ArrayList<>(snapshots)));
        return map;
    }

    private static InventoryHistorySavedData fromMap(Map<UUID, List<InventorySnapshot>> map) {
        InventoryHistorySavedData data = new InventoryHistorySavedData();
        map.forEach((uuid, snapshots) -> data.history.put(uuid, new ArrayDeque<>(snapshots)));
        return data;
    }
}
