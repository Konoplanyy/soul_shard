package me.konoplanyy.soulshard.mixin;

import me.konoplanyy.soulshard.data.ShardRecord;
import me.konoplanyy.soulshard.data.ShardStorage;
import me.konoplanyy.soulshard.entity.SoulShardEntity;
import me.konoplanyy.soulshard.registry.ModEntities;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(Player.class)
public class PlayerEntityMixin {
    @Inject(method = "dropEquipment", at = @At("HEAD"), cancellable = true)
    private void onDropEquipment(CallbackInfo ci) {
        System.out.println("Речі не падають)");

        Player player = (Player) (Object) this;

        SoulShardEntity soulShard = new SoulShardEntity(ModEntities.SOUL_SHARD.get(), player.level());
        soulShard.setOwner(player);

        var playerInv = player.getInventory();
        var crystalInv = soulShard.getInventory();

        for (int i = 0; i < 36; i++) {
            ItemStack stack = playerInv.items.get(i);
            if (!stack.isEmpty()) {
                crystalInv.setItem(i, stack.copy());
            }
        }

        for (int i = 0; i < 4; i++) {
            ItemStack stack = playerInv.armor.get(i);
            if (!stack.isEmpty()) {
                crystalInv.setItem(36 + i, stack.copy());
            }
        }

        ItemStack offhand = playerInv.offhand.get(0);
        if (!offhand.isEmpty()) {
            crystalInv.setItem(40, offhand.copy());
        }

        playerInv.clearContent();

        if (!crystalInv.isEmpty()) {
            soulShard.setPos(player.getX(), player.getY(), player.getZ());

            if (player.level() instanceof ServerLevel serverLevel) {
                UUID shardId = UUID.randomUUID();
                soulShard.setShardId(shardId);

                NonNullList<ItemStack> items = NonNullList.withSize(crystalInv.getContainerSize(), ItemStack.EMPTY);
                for (int i = 0; i < crystalInv.getContainerSize(); i++) {
                    items.set(i, crystalInv.getItem(i));
                }

                ShardRecord shard = new ShardRecord(shardId, player.getUUID(), System.currentTimeMillis(), items);
                ShardStorage.get(serverLevel).addShard(shard);
            }

            player.level().addFreshEntity(soulShard);
        }

        ci.cancel();
    }
}