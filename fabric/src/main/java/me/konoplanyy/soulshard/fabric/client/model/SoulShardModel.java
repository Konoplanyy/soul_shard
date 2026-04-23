package me.konoplanyy.soulshard.fabric.client.model;

import me.konoplanyy.soulshard.fabric.client.entity.SoulShardEntityFabric;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SoulShardModel extends GeoModel<SoulShardEntityFabric> {
    @Override
    public ResourceLocation getModelResource(SoulShardEntityFabric entity) {
        return new ResourceLocation("soulshard", "geo/soul_shard.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SoulShardEntityFabric entity) {
        return new ResourceLocation("soulshard", "textures/entity/soul_shard.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SoulShardEntityFabric entity) {
        return new ResourceLocation("soulshard", "animations/soul_shard.animation.json");
    }
}