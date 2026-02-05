package me.konoplanyy.soulshard.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import me.konoplanyy.soulshard.entity.SoulShardEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create("soulshard", Registries.ENTITY_TYPE);
    public static final RegistrySupplier<EntityType<SoulShardEntity>> SOUL_SHARD =
            ENTITIES.register("soul_shard", () ->
                EntityType.Builder.of(SoulShardEntity::new, MobCategory.MISC)
                        .sized(0.5f, 2f)
                        .build("soul_shard"));

    public static void init(){
        ENTITIES.register();
    }
}
