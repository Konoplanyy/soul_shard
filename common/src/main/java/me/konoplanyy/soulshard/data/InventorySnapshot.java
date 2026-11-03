package me.konoplanyy.soulshard.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public record InventorySnapshot(List<ItemStack> items, long timestamp) {

    public static final Codec<InventorySnapshot> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemStack.OPTIONAL_CODEC.listOf().fieldOf("items").forGetter(InventorySnapshot::items),
            Codec.LONG.fieldOf("timestamp").forGetter(InventorySnapshot::timestamp)
    ).apply(instance, InventorySnapshot::new));

    public static InventorySnapshot fromPlayer(Player player) {
        List<ItemStack> items = new ArrayList<>(41);
        var inv = player.getInventory();

        for (int i = 0; i < 41; i++)
            items.add(inv.getItem(i).copy());

        return new InventorySnapshot(items, System.currentTimeMillis());
    }
}
