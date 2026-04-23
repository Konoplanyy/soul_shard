package me.konoplanyy.soulshard.entity;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;

public class EntityFactory {
    @ExpectPlatform
    public static SoulShardEntity createSoulShard(EntityType<? extends PathfinderMob> type, Level level) {
        throw new AssertionError();
    }
}