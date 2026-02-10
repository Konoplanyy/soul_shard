package me.konoplanyy.soulshard.fabric.client;

import me.konoplanyy.soulshard.client.render.SoulShardRenderer;
import me.konoplanyy.soulshard.entity.SoulShardEntity;
import me.konoplanyy.soulshard.registry.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;

public final class SoulShardModFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        EntityRendererRegistry.register(ModEntities.SOUL_SHARD.get(), SoulShardRenderer::new);

        FabricDefaultAttributeRegistry.register(ModEntities.SOUL_SHARD.get(), SoulShardEntity.createAttributes());
    }
}
