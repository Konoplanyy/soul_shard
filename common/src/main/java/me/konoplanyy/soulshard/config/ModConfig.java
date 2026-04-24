package me.konoplanyy.soulshard.config;

public interface ModConfig {
    int getCrystalLifeTime();
    boolean canDie();
    boolean dropItemsOnBreak();
    boolean DeadChatNotify();
    boolean DespawnChatNotify();
    int getCrystalHealth();
}
