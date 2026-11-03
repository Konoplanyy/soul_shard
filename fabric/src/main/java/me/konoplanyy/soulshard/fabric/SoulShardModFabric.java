package me.konoplanyy.soulshard.fabric;

import me.konoplanyy.soulshard.SoulShardMod;
import me.konoplanyy.soulshard.entity.SoulShardEntity;
import me.konoplanyy.soulshard.registry.ModEntities;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;

public final class SoulShardModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        SoulShardMod.init();

        // Атрибути потрібні і на сервері, а не лише на клієнті
        FabricDefaultAttributeRegistry.register(ModEntities.SOUL_SHARD.get(), SoulShardEntity.createAttributes());
    }
}
