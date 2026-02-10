package me.konoplanyy.soulshard.neoforge;

import me.konoplanyy.soulshard.SoulShardMod;
import me.konoplanyy.soulshard.config.neoforge.NeoForgeSoulShardConfig;
import me.konoplanyy.soulshard.entity.SoulShardEntity;
import me.konoplanyy.soulshard.registry.ModEntities;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

@Mod(SoulShardMod.MOD_ID)
public final class SoulShardModNeoForge {
    public SoulShardModNeoForge(ModContainer container, IEventBus modEventBus) {
        // 1. РЕЄСТРУЄМО ВСЕ ВІДРАЗУ
        SoulShardMod.init();

        // 2. РЕЄСТРУЄМО АТРИБУТИ (Це виправить помилку з /summon)
        modEventBus.addListener(this::registerAttributes);

        container.registerConfig(ModConfig.Type.COMMON, NeoForgeSoulShardConfig.SPEC);
    }

    private void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.SOUL_SHARD.get(), SoulShardEntity.createAttributes().build());
    }
}