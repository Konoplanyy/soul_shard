package me.konoplanyy.soulshard.config.fabric;

import me.konoplanyy.soulshard.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

public class SoulShardConfigImpl {
    private static boolean initialized = false;

    public static ModConfig getConfig() {
        if (!initialized) {
            // Реєструємо конфіг при першому зверненні
            AutoConfig.register(FabricSoulShardConfig.class, GsonConfigSerializer::new);
            initialized = true;
        }
        return AutoConfig.getConfigHolder(FabricSoulShardConfig.class).getConfig();
    }
}
