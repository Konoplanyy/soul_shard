package me.konoplanyy.soulshard.neoforge;

import net.neoforged.fml.common.Mod;

import me.konoplanyy.soulshard.ExampleMod;

@Mod(ExampleMod.MOD_ID)
public final class ExampleModNeoForge {
    public ExampleModNeoForge() {
        // Run our common setup.
        ExampleMod.init();
    }
}
