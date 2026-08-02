package me.konoplanyy.soulshard;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.event.EventResult;
import me.konoplanyy.soulshard.command.SoulShardCommands;
import me.konoplanyy.soulshard.registry.ModEntities;
import net.minecraft.world.entity.player.Player;


public final class SoulShardMod {
    public static final String MOD_ID = "soul_shard";

    public static void init() {
        CommandRegistrationEvent.EVENT.register((dispatcher, registryAccess, environment) -> {
            SoulShardCommands.register(dispatcher);
        });

        ModEntities.init();
    }
}
