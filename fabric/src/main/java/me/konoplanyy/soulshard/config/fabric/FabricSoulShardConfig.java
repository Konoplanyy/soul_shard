package me.konoplanyy.soulshard.config.fabric;

import me.konoplanyy.soulshard.config.ModConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "soulshard")
public class FabricSoulShardConfig implements ConfigData, ModConfig {

    @ConfigEntry.Gui.Tooltip
    public int crystalLifeTime = 300;

    @ConfigEntry.Gui.Tooltip
    public boolean canDie = true;

    @ConfigEntry.Gui.Tooltip
    public boolean dropItemsOnBreak = true;

    @ConfigEntry.Gui.Tooltip
    public int crystalHealth = 10;

    @Override
    public int getCrystalLifeTime() {
        return crystalLifeTime;
    }

    @Override
    public boolean canDie() {
        return canDie;
    }

    @Override
    public boolean dropItemsOnBreak() {
        return dropItemsOnBreak;
    }

    @Override
    public int getCrystalHealth() {
        return crystalHealth;
    }
}
