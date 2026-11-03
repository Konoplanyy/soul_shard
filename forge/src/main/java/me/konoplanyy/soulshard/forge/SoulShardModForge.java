package me.konoplanyy.soulshard.forge;

import dev.architectury.platform.forge.EventBuses;
import me.konoplanyy.soulshard.SoulShardMod;
import me.konoplanyy.soulshard.config.forge.ForgeSoulShardConfig;
import me.konoplanyy.soulshard.forge.client.entity.SoulShardEntityForge;
import me.konoplanyy.soulshard.registry.ModEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(SoulShardMod.MOD_ID)
public final class SoulShardModForge {
    public SoulShardModForge() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Architectury потребує шину мода для реєстрів
        EventBuses.registerModEventBus(SoulShardMod.MOD_ID, modEventBus);

        // Реєструємо все відразу
        SoulShardMod.init();

        // Атрибути потрібні і на сервері
        modEventBus.addListener(this::registerAttributes);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ForgeSoulShardConfig.SPEC);
    }

    private void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(
                (EntityType<SoulShardEntityForge>) (EntityType<?>) ModEntities.SOUL_SHARD.get(),
                SoulShardEntityForge.createAttributes().build()
        );
    }
}
