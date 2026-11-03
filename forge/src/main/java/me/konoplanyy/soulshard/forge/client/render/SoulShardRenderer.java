package me.konoplanyy.soulshard.forge.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import me.konoplanyy.soulshard.forge.client.entity.SoulShardEntityForge;
import me.konoplanyy.soulshard.forge.client.model.SoulShardModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SoulShardRenderer extends GeoEntityRenderer<SoulShardEntityForge> {
    public SoulShardRenderer(EntityRendererProvider.Context context) {
        super(context, new SoulShardModel());
    }

    @Override
    public RenderType getRenderType(SoulShardEntityForge animatable, ResourceLocation texture,
                                    @Nullable MultiBufferSource bufferSource, float partialTicks) {
        return RenderType.entityTranslucent(texture);
    }

    @Override
    public void scaleModelForRender(float widthScale, float heightScale, PoseStack poseStack,
                                       SoulShardEntityForge animatable, software.bernie.geckolib.cache.object.BakedGeoModel model,
                                       boolean isReRender, float partialTick, int packedLight, int packedOverlay) {
        poseStack.scale(5.0f, 5.0f, 5.0f);
    }
}