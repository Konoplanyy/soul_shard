package me.konoplanyy.soulshard.client.render;

import me.konoplanyy.soulshard.client.model.SoulShardModel;
import me.konoplanyy.soulshard.entity.SoulShardEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SoulShardRenderer extends GeoEntityRenderer<SoulShardEntity, SoulShardRenderState> {
    public SoulShardRenderer(EntityRendererProvider.Context context){
        super(context, new SoulShardModel());
    }

    @Override
    public SoulShardRenderState createRenderState(SoulShardEntity animatable, Void relatedObject) {
        return new SoulShardRenderState();
    }

    @Override
    public RenderType getRenderType(SoulShardRenderState renderState, ResourceLocation texture){
        return RenderType.entityTranslucent(texture);
    }
}
