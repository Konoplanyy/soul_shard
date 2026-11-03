package me.konoplanyy.soulshard.forge.client;

import me.konoplanyy.soulshard.SoulShardMod;
import me.konoplanyy.soulshard.forge.client.entity.SoulShardEntityForge;
import me.konoplanyy.soulshard.forge.client.render.SoulShardRenderer;
import me.konoplanyy.soulshard.registry.ModEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SoulShardMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class SoulShardModForgeClient {
    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(
                (EntityType<SoulShardEntityForge>) (EntityType<?>) ModEntities.SOUL_SHARD.get(),
                SoulShardRenderer::new
        );
    }
}
