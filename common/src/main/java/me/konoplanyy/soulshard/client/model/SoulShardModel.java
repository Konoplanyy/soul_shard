package me.konoplanyy.soulshard.client.model;

import me.konoplanyy.soulshard.entity.SoulShardEntity;
import net.minecraft.resources.Identifier;
import com.geckolib.model.GeoModel;
import com.geckolib.renderer.base.GeoRenderState;

public class SoulShardModel extends GeoModel<SoulShardEntity> {
    @Override
    public Identifier getModelResource(GeoRenderState renderState) {
        return Identifier.fromNamespaceAndPath("soulshard", "geo/soul_shard.geo.json");
    }

    @Override
    public Identifier getTextureResource(GeoRenderState renderState){
        return Identifier.fromNamespaceAndPath("soulshard", "textures/entity/soul_shard.png");
    }

    @Override
    public Identifier getAnimationResource(SoulShardEntity entity){
        return Identifier.fromNamespaceAndPath("soulshard", "animations/soul_shard.animation.json");
    }
}
