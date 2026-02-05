package me.konoplanyy.soulshard.client.render;

import me.konoplanyy.soulshard.client.model.SoulShardModel;
import me.konoplanyy.soulshard.entity.SoulShardEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SoulShardRenderer extends GeoEntityRenderer<SoulShardEntity> {
    public SoulShardRenderer(EntityRendererProvider.Context context){
        super(context, new SoulShardModel());
    }
}
