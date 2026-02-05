package me.konoplanyy.soulshard.fabric;

import me.konoplanyy.soulshard.client.render.SoulShardRenderer;
import me.konoplanyy.soulshard.registry.ModEntities;
import net.fabricmc.api.ModInitializer;

import me.konoplanyy.soulshard.ExampleMod;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public final class ExampleModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        ExampleMod.init();
    }
}
