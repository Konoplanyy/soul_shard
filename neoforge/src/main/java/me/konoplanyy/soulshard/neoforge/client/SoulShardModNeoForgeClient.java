package me.konoplanyy.soulshard.neoforge.client;

import me.konoplanyy.soulshard.client.render.SoulShardRenderer;
import me.konoplanyy.soulshard.registry.ModEntities;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = "soul_shard", bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class SoulShardModNeoForgeClient {

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        // Використовуй .get() тільки якщо впевнений, або перевір на наявність
        if (ModEntities.SOUL_SHARD != null) {
            event.registerEntityRenderer(ModEntities.SOUL_SHARD.get(), SoulShardRenderer::new);
        }
    }
}