package me.konoplanyy.soulshard.config.neoforge;

import me.konoplanyy.soulshard.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

public class NeoForgeSoulShardConfig implements ModConfig {
    public static final NeoForgeSoulShardConfig INSTANCE = new NeoForgeSoulShardConfig();
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.IntValue CRYSTAL_LIFE_TIME;
    private static final ModConfigSpec.BooleanValue CAN_DIE;
    private static final ModConfigSpec.BooleanValue DROP_ITEMS_ON_BREAK;
    private static final ModConfigSpec.BooleanValue DESPAWN_CHAT_NOTIFY;
    private static final ModConfigSpec.BooleanValue DEAD_CHAT_NOTIFY;
    private static final ModConfigSpec.IntValue CRYSTAL_HEALTH;

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
                .define("dropItemsOnBreak", true);

        DEAD_CHAT_NOTIFY = BUILDER
                .comment("Sends a message to the player when their Soul Shard dies.")
                .define("dropItemsOnBreak", true);

        BUILDER.pop();
    }

    public static final ModConfigSpec SPEC = BUILDER.build();
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
