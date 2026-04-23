package me.konoplanyy.soulshard.fabric.client;

import me.konoplanyy.soulshard.fabric.client.entity.SoulShardEntityFabric;
import me.konoplanyy.soulshard.fabric.client.render.SoulShardRenderer;
import me.konoplanyy.soulshard.registry.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.world.entity.EntityType;

public final class SoulShardModFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(
                (EntityType<SoulShardEntityFabric>) (EntityType<?>) ModEntities.SOUL_SHARD.get(),
                SoulShardRenderer::new
        );
        FabricDefaultAttributeRegistry.register(ModEntities.SOUL_SHARD.get(), SoulShardEntityFabric.createAttributes());
    }
}