package me.konoplanyy.soulshard.config.forge;

import me.konoplanyy.soulshard.config.ModConfig;
import net.minecraftforge.common.ForgeConfigSpec;

public class ForgeSoulShardConfig implements ModConfig {
    public static final ForgeSoulShardConfig INSTANCE = new ForgeSoulShardConfig();

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.IntValue CRYSTAL_LIFE_TIME;
    private static final ForgeConfigSpec.BooleanValue CAN_DIE;
    private static final ForgeConfigSpec.BooleanValue DROP_ITEMS_ON_BREAK;
    private static final ForgeConfigSpec.BooleanValue DESPAWN_CHAT_NOTIFY;
    private static final ForgeConfigSpec.BooleanValue DEAD_CHAT_NOTIFY;
    private static final ForgeConfigSpec.IntValue CRYSTAL_HEALTH;

    static {
        BUILDER.push("Soul Shard Settings");

        CRYSTAL_LIFE_TIME = BUILDER
                .comment("Crystal lifetime in seconds (-1 = infinite)")
                .defineInRange("crystalLifeTime", 300, -1, Integer.MAX_VALUE);

        CAN_DIE = BUILDER
                .comment("Can die")
                .define("canDie", true);

        DROP_ITEMS_ON_BREAK = BUILDER
                .comment("Do items drop when a crystal is destroyed")
                .define("dropItemsOnBreak", true);

        CRYSTAL_HEALTH = BUILDER
                .comment("amount of health in the crystal")
                .defineInRange("crystalHealth", 10, 1, Integer.MAX_VALUE);

        DESPAWN_CHAT_NOTIFY = BUILDER
                .comment("Toggle chat notifications. If true, players will receive a red message when their Soul Shard's lifetime expires.")
                .define("despawnChatNotify", true);

        DEAD_CHAT_NOTIFY = BUILDER
                .comment("Sends a message to the player when their Soul Shard dies.")
                .define("deadChatNotify", true);

        BUILDER.pop();
    }

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    @Override
    public int getCrystalLifeTime() {
        return CRYSTAL_LIFE_TIME.get();
    }

    @Override
    public boolean canDie() {
        return CAN_DIE.get();
    }

    @Override
    public boolean dropItemsOnBreak() {
        return DROP_ITEMS_ON_BREAK.get();
    }

    @Override
    public boolean DeadChatNotify() {
        return DEAD_CHAT_NOTIFY.get();
    }

    @Override
    public boolean DespawnChatNotify() {
        return DESPAWN_CHAT_NOTIFY.get();
    }

    @Override
    public int getCrystalHealth() {
        return CRYSTAL_HEALTH.get();
    }
}
