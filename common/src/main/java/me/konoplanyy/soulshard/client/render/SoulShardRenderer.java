package me.konoplanyy.soulshard.client.render;

import me.konoplanyy.soulshard.client.model.SoulShardModel;
import me.konoplanyy.soulshard.entity.SoulShardEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SoulShardRenderer extends GeoEntityRenderer<SoulShardEntity> {
    public SoulShardRenderer(EntityRendererProvider.Context context){
        super(context, new SoulShardModel());
    }

    @Override
    public RenderType getRenderType(SoulShardEntity animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTicks){
        return RenderType.entityTranslucent(texture);
    }
}
