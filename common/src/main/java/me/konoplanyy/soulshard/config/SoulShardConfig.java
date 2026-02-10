package me.konoplanyy.soulshard.config;

import dev.architectury.injectables.annotations.ExpectPlatform;

public class SoulShardConfig {
    @ExpectPlatform
    public static ModConfig getConfig(){
        throw new AssertionError();
    }
}
