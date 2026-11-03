package me.konoplanyy.soulshard.entity.forge;

import me.konoplanyy.soulshard.forge.client.entity.SoulShardEntityForge;
import me.konoplanyy.soulshard.entity.SoulShardEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;

public class EntityFactoryImpl {
    public static SoulShardEntity createSoulShard(EntityType<? extends PathfinderMob> type, Level level) {
        return new SoulShardEntityForge(type, level);
    }
}