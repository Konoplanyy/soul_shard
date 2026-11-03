package me.konoplanyy.soulshard.client.model;

import me.konoplanyy.soulshard.entity.SoulShardEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class SoulShardModel extends GeoModel<SoulShardEntity> {
    @Override
    public ResourceLocation getModelResource(SoulShardEntity entity, GeoRenderer<SoulShardEntity> renderer) {
        return ResourceLocation.fromNamespaceAndPath("soulshard", "geo/soul_shard.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SoulShardEntity entity, GeoRenderer<SoulShardEntity> renderer){
        return ResourceLocation.fromNamespaceAndPath("soulshard", "textures/entity/soul_shard.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SoulShardEntity entity){
        return ResourceLocation.fromNamespaceAndPath("soulshard", "animations/soul_shard.animation.json");
    }
}
