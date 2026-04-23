package me.konoplanyy.soulshard.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import me.konoplanyy.soulshard.SoulShardMod;
import me.konoplanyy.soulshard.entity.SoulShardEntity;
import me.konoplanyy.soulshard.entity.EntityFactory; // Додай імпорт
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(SoulShardMod.MOD_ID, Registries.ENTITY_TYPE);

    public static final RegistrySupplier<EntityType<SoulShardEntity>> SOUL_SHARD =
            ENTITIES.register("soul_shard", () ->
                    EntityType.Builder.of(EntityFactory::createSoulShard, MobCategory.MISC)
                            .sized(0.5f, 2.0f)
                            .build("soul_shard"));

    public static void init(){
        ENTITIES.register();
    }
}