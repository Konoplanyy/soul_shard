package me.konoplanyy.soulshard;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import me.konoplanyy.soulshard.command.SoulShardCommand;
import me.konoplanyy.soulshard.registry.ModEntities;


public final class SoulShardMod {
    public static final String MOD_ID = "soul_shard";

    public static void init() {
        CommandRegistrationEvent.EVENT.register((dispatcher, registry, selection) ->
                SoulShardCommand.register(dispatcher)
        );

        ModEntities.init();
    }
}
