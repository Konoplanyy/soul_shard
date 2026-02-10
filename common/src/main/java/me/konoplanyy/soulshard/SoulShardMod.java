package me.konoplanyy.soulshard;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.event.EventResult;
import me.konoplanyy.soulshard.registry.ModEntities;
import net.minecraft.world.entity.player.Player;


public final class SoulShardMod {
    public static final String MOD_ID = "soul_shard";

    public static void init() {
        EntityEvent.LIVING_DEATH.register((entity, source) -> {
            if (entity instanceof Player player){
                if (!player.level().isClientSide){
                    System.out.println("Thats Work!!! player name: " + player.getName().getString());
                }
            }

            return EventResult.pass();
        });

        ModEntities.init();
    }
}
