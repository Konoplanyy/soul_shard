package me.konoplanyy.soulshard.forge.client.model;

import me.konoplanyy.soulshard.forge.client.entity.SoulShardEntityForge;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SoulShardModel extends GeoModel<SoulShardEntityForge> {
    @Override
    public ResourceLocation getModelResource(SoulShardEntityForge entity) {
        return new ResourceLocation("soulshard", "geo/soul_shard.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SoulShardEntityForge entity) {
        return new ResourceLocation("soulshard", "textures/entity/soul_shard.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SoulShardEntityForge entity) {
        return new ResourceLocation("soulshard", "animations/soul_shard.animation.json");
    }
}